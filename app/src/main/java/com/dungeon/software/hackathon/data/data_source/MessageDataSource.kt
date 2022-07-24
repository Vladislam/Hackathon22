package com.dungeon.software.hackathon.data.data_source

import com.dangeon.software.notes.util.pop_up.CustomError
import com.dungeon.software.hackathon.data.models.MessageDataDto
import com.dungeon.software.hackathon.data.models.MessageDto
import com.dungeon.software.hackathon.data.models.MessageGroupDto
import com.dungeon.software.hackathon.util.DataState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface MessageDataSource {

    fun getLastMessage(chatId: String): Flow<DataState<MessageDataDto?>>

    suspend fun sendMessage(message: MessageDataDto, chatId: String)

    fun getMessages(chatId: String): Flow<DataState<ArrayList<MessageDataDto>>>

    fun cancelLastMessagesSubscription()
    fun cancelMessageSubscription()

    class Base : MessageDataSource {

        private val firestore = FirebaseFirestore.getInstance()
        private val lastMessagesSubscription = arrayListOf<ListenerRegistration>()
        private var messagesSubscription: ListenerRegistration? = null

        private val scope = CoroutineScope(Dispatchers.IO)

        private val messages = "messages"

        override fun getLastMessage(chatId: String) = callbackFlow<DataState<MessageDataDto?>> {
            FirebaseAuth.getInstance().currentUser?.also { user ->
                val subs: ListenerRegistration = firestore
                    .collection(ChatDataSource.collectionChat)
                    .document(chatId)
                    .collection(messages)
                    .orderBy("timeSent")
                    .limitToLast(1)
                    .addSnapshotListener { snapshot, error ->
                        scope.launch {
                            error?.let {
                                this@callbackFlow.send(DataState.Error(it))
                            }
                            snapshot?.let { data ->
                                try {
                                    data.toObjects(MessageDto::class.java).also {
                                        send(DataState.Data(it.toCollection(ArrayList()).firstOrNull()))
                                    }
                                } catch (e: Throwable) {
                                    data.toObjects(MessageGroupDto::class.java).run {
                                        send(
                                            DataState.Data(
                                                this.toCollection(ArrayList()).firstOrNull()
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                awaitClose { subs.remove() }
            } ?: run {
                send(DataState.Error(CustomError.SomethingWentWrong))
            }
        }

        override suspend fun sendMessage(message: MessageDataDto, chatId: String): Unit =
            suspendCoroutine { emitter ->
                val user = FirebaseAuth.getInstance().currentUser ?: run {
                    emitter.resumeWithException(NullPointerException())
                    return@suspendCoroutine
                }
                firestore
                    .collection(ChatDataSource.collectionChat)
                    .document(chatId)
                    .collection(messages)
                    .add(message)
                    .addOnSuccessListener {
                        emitter.resume(Unit)
                    }
                    .addOnFailureListener {
                        emitter.resumeWithException(it)
                    }
            }

        override fun getMessages(chatId: String) =
            callbackFlow<DataState<ArrayList<MessageDataDto>>> {
                FirebaseAuth.getInstance().currentUser?.also { user ->
                    val subscription = firestore
                        .collection(ChatDataSource.collectionChat)
                        .document(chatId)
                        .collection(messages)
                        .addSnapshotListener { snapshot, error ->
                            scope.launch {
                                error?.let {
                                    this@callbackFlow.send(DataState.Error(it))
                                }
                                snapshot?.let { data ->
                                    try {
                                        data.toObjects(MessageDto::class.java).also {
                                            send(DataState.Data(it.toCollection(ArrayList())))
                                        }
                                    } catch (e: Throwable) {
                                        Timber.v(e)
                                        data.toObjects(MessageGroupDto::class.java).run {
                                            send(DataState.Data(this.toCollection(ArrayList())))
                                        }
                                    }
                                }
                            }
                        }
                    awaitClose {
                        subscription.remove()
                    }
                } ?: run {
                    send(DataState.Error(CustomError.SomethingWentWrong))
                }
            }

        override fun cancelLastMessagesSubscription() {
            lastMessagesSubscription.forEach {
                it.remove()
            }
            lastMessagesSubscription.clear()
        }

        override fun cancelMessageSubscription() {
            messagesSubscription?.remove()
            messagesSubscription = null
        }

    }

}