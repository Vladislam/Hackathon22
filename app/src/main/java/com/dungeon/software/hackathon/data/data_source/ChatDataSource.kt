package com.dungeon.software.hackathon.data.data_source

import com.dangeon.software.notes.util.pop_up.CustomError
import com.dungeon.software.hackathon.data.models.ChatDataDto
import com.dungeon.software.hackathon.data.models.ChatDto
import com.dungeon.software.hackathon.data.models.GroupChatDto
import com.dungeon.software.hackathon.util.DataState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface ChatDataSource {

    suspend fun createChat(chatModel: ChatDataDto): String

    suspend fun getChat(id: String): ChatDataDto

    suspend fun changeGroupImage(id: String, image: String)

    suspend fun changeGroupName(id: String, name: String)

    fun getChats(): Flow<DataState<ArrayList<ChatDto>>>

    fun cancelChatSubscription()

    class Base : ChatDataSource {

        private val firestore = FirebaseFirestore.getInstance()

        private val collectionChat = "chat"
        private val collection = "collection"

        private val scope = CoroutineScope(Dispatchers.IO)

        private var chatSubscription: ListenerRegistration? = null

        override suspend fun createChat(chatModel: ChatDataDto) = suspendCoroutine { emitter ->
            val user = FirebaseAuth.getInstance().currentUser ?: run {
                emitter.resumeWithException(NullPointerException())
                return@suspendCoroutine
            }
            firestore
                .collection(collectionChat)
                .document(user.uid)
                .collection(collection)
                .add(chatModel)
                .addOnSuccessListener {
                    emitter.resume(it.id)
                }
                .addOnFailureListener {
                    emitter.resumeWithException(it)
                }
        }

        override suspend fun getChat(id: String) = suspendCoroutine { emitter ->
            val user = FirebaseAuth.getInstance().currentUser ?: run {
                emitter.resumeWithException(NullPointerException())
                return@suspendCoroutine
            }
            firestore
                .collection(collectionChat)
                .document(user.uid)
                .collection(collection)
                .document(id)
                .get()
                .addOnSuccessListener { data ->
                    try {
                        data.toObject(ChatDto::class.java)?.also {
                            emitter.resume(it)
                        } ?: kotlin.run { emitter.resumeWithException(NullPointerException()) }
                    } catch (e: Exception) {
                        data.toObject(GroupChatDto::class.java)?.also {
                            emitter.resume(it)
                        } ?: kotlin.run { emitter.resumeWithException(NullPointerException()) }
                    }
                }
                .addOnFailureListener {
                    emitter.resumeWithException(it)
                }
        }

        override suspend fun changeGroupImage(id: String, image: String) {
            isGroupChat(id)
            changeGroupImageWithoutChech(id, image)
        }

        private suspend fun changeGroupImageWithoutChech(id: String, image: String) = suspendCoroutine { emitter ->
            val user = FirebaseAuth.getInstance().currentUser ?: run {
                emitter.resumeWithException(NullPointerException())
                return@suspendCoroutine
            }
            firestore
                .collection(collectionChat)
                .document(user.uid)
                .collection(collection)
                .document(id)
                .update(mapOf("imageUrl" to image))
                .addOnSuccessListener {
                    emitter.resume(Unit)
                }
                .addOnFailureListener {
                    emitter.resumeWithException(it)
                }
        }

        override suspend fun changeGroupName(id: String, name: String) {
            isGroupChat(id)
            changeGroupNameWithoutChech(id, name)
        }

        private suspend fun changeGroupNameWithoutChech(id: String, name: String) = suspendCoroutine { emitter ->
            val user = FirebaseAuth.getInstance().currentUser ?: run {
                emitter.resumeWithException(NullPointerException())
                return@suspendCoroutine
            }
            firestore
                .collection(collectionChat)
                .document(user.uid)
                .collection(collection)
                .document(id)
                .update(mapOf("imageUrl" to name))
                .addOnSuccessListener {
                    emitter.resume(Unit)
                }
                .addOnFailureListener {
                    emitter.resumeWithException(it)
                }
        }


        override fun getChats() = flow<DataState<ArrayList<ChatDto>>> {
            FirebaseAuth.getInstance().currentUser?.also { user ->
                cancelChatSubscription()
                chatSubscription = firestore
                    .collection(collectionChat)
                    .document(user.uid)
                    .collection(collection)
                    .addSnapshotListener { snapshot, error ->
                        scope.launch {
                            error?.let {
                                this@flow.emit(DataState.Error(it))
                            }
                            snapshot?.let {
                                this@flow.emit(DataState.Data(it.toObjects(ChatDto::class.java).toCollection(ArrayList())))
                            }
                        }
                    }
            } ?: run {
                emit(DataState.Error(CustomError.SomethingWentWrong))
            }
        }

        private suspend fun isGroupChat(id: String) = suspendCoroutine { emitter ->
            val user = FirebaseAuth.getInstance().currentUser ?: run {
                emitter.resumeWithException(NullPointerException())
                return@suspendCoroutine
            }
            firestore
                .collection(collectionChat)
                .document(user.uid)
                .collection(collection)
                .document(id)
                .get()
                .addOnSuccessListener {
                    it.toObject(GroupChatDto::class.java)?.also {
                        emitter.resume(Unit)
                    } ?: kotlin.run {
                        emitter.resumeWithException(NullPointerException())
                    }
                }
        }

        override fun cancelChatSubscription() {
            chatSubscription?.remove()
            chatSubscription = null
        }
    }


}