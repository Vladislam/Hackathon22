package com.dungeon.software.hackathon.data.data_source

import com.dangeon.software.notes.util.pop_up.CustomError
import com.dungeon.software.hackathon.data.models.ChatDataDto
import com.dungeon.software.hackathon.data.models.ChatDto
import com.dungeon.software.hackathon.data.models.GroupChatDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface ChatDataSource {

    suspend fun createChat(chatModel: ChatDataDto): String

    suspend fun getChat(id: String): ChatDataDto

    suspend fun getPeerToPeerChatByOpponent(opponentId: String): ChatDataDto?

    suspend fun changeGroupImage(id: String, image: String)

    suspend fun changeGroupName(id: String, name: String)

    suspend fun getChats(): ArrayList<ChatDataDto>

    companion object {
        val collectionChat = "chat"
        val collection = "collection"
    }

    class Base : ChatDataSource {

        private val firestore = FirebaseFirestore.getInstance()

        private val scope = CoroutineScope(Dispatchers.IO)

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

        override suspend fun getPeerToPeerChatByOpponent(opponentId: String): ChatDataDto? = suspendCoroutine { emitter ->
            val user = FirebaseAuth.getInstance().currentUser ?: run {
                emitter.resumeWithException(NullPointerException())
                return@suspendCoroutine
            }
            firestore
                .collection(collectionChat)
                .document(user.uid)
                .collection(collection)
                .whereEqualTo("opponent", opponentId)
                .get()
                .addOnSuccessListener {
                    emitter.resume(it.toObjects(ChatDto::class.java).firstOrNull())
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


        override suspend fun getChats() = suspendCoroutine<ArrayList<ChatDataDto>> { emitter ->
            FirebaseAuth.getInstance().currentUser?.also { user ->
                firestore
                    .collection(collectionChat)
                    .document(user.uid)
                    .collection(collection)
                    .get()
                    .addOnSuccessListener {
                        scope.launch {
                            try {
                                emitter.resume(it.toObjects(ChatDto::class.java).toCollection(ArrayList()))
                            } catch (e: Throwable) {
                                emitter.resume(it.toObjects(GroupChatDto::class.java).toCollection(ArrayList()))
                            }
                        }
                    }
                    .addOnFailureListener {
                        emitter.resumeWithException(it)
                    }
            } ?: run {
                emitter.resumeWithException(CustomError.SomethingWentWrong)
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
    }


}