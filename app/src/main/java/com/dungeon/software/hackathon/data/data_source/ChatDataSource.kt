package com.dungeon.software.hackathon.data.data_source

import com.dangeon.software.notes.util.pop_up.CustomError
import com.dungeon.software.hackathon.data.models.ChatDataDto
import com.dungeon.software.hackathon.data.models.ChatDto
import com.dungeon.software.hackathon.data.models.GroupChatDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalStateException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface ChatDataSource {

    suspend fun createChat(chatModel: ChatDataDto): String

    suspend fun getChat(id: String): ChatDataDto

    suspend fun getPeerToPeerChatByOpponent(opponentId: String): ChatDataDto?

    suspend fun changeGroupImage(id: String, image: String)

    suspend fun changeGroupName(id: String, name: String)

    suspend fun getChatsByOpponent(): ArrayList<ChatDataDto>

    suspend fun getChatsByCreator(): ArrayList<ChatDataDto>

    companion object {
        val collectionChat = "chat"
    }

    class Base : ChatDataSource {

        private val firestore = FirebaseFirestore.getInstance()

        private val scope = CoroutineScope(Dispatchers.IO)

        override suspend fun createChat(chatModel: ChatDataDto) = suspendCoroutine { emitter ->
            firestore
                .collection(collectionChat)
                .add(chatModel)
                .addOnSuccessListener {
                    emitter.resume(it.id)
                }
                .addOnFailureListener {
                    emitter.resumeWithException(it)
                }
        }

        override suspend fun getChat(id: String) = suspendCoroutine { emitter ->
            firestore
                .collection(collectionChat)
                .document(id)
                .get()
                .addOnSuccessListener { data ->
                    try {
                        data.toObject(ChatDto::class.java)?.also {
                            if (it.opponent.isEmpty()) {
                                throw IllegalStateException()
                            }
                            emitter.resume(it)
                        } ?: kotlin.run { emitter.resumeWithException(NullPointerException()) }
                    } catch (e: Exception) {
                        data.toObject(GroupChatDto::class.java)?.also {
                            if (it.opponents.isEmpty()) {
                                throw IllegalStateException()
                            }
                            emitter.resume(it)
                        } ?: kotlin.run { emitter.resumeWithException(NullPointerException()) }
                    }
                }
                .addOnFailureListener {
                    emitter.resumeWithException(it)
                }
        }

        override suspend fun getPeerToPeerChatByOpponent(opponentId: String): ChatDataDto? = suspendCoroutine { emitter ->
            firestore
                .collection(collectionChat)
                .whereEqualTo("creator", FirebaseAuth.getInstance().currentUser?.uid)
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
            changeGroupImageWithoutCheck(id, image)
        }

        private suspend fun changeGroupImageWithoutCheck(id: String, image: String) = suspendCoroutine { emitter ->
            val user = FirebaseAuth.getInstance().currentUser ?: run {
                emitter.resumeWithException(NullPointerException())
                return@suspendCoroutine
            }
            firestore
                .collection(collectionChat)
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
            firestore
                .collection(collectionChat)
                .document(id)
                .update(mapOf("imageUrl" to name))
                .addOnSuccessListener {
                    emitter.resume(Unit)
                }
                .addOnFailureListener {
                    emitter.resumeWithException(it)
                }
        }


        override suspend fun getChatsByOpponent() = suspendCoroutine { emitter ->
            FirebaseAuth.getInstance().currentUser?.also { user ->
                firestore
                    .collection(collectionChat)
                    .whereEqualTo("opponent", FirebaseAuth.getInstance().currentUser?.uid)
                    .get()
                    .addOnSuccessListener {
                        scope.launch {
                            emitter.resume(it.documents.mapNotNull {
                                try {
                                    it.toObject(GroupChatDto::class.java)?.apply {
                                        if (opponents?.isEmpty() == true) {
                                            throw IllegalStateException()
                                        }
                                    }
                                } catch (e: Throwable) {
                                    it.toObject(ChatDto::class.java)?.apply {
                                        if (opponent?.isEmpty() == true) {
                                            throw IllegalStateException()
                                        }
                                    }
                                }
                            }.toCollection(ArrayList()))
                        }
                    }
                    .addOnFailureListener {
                        emitter.resumeWithException(it)
                    }
            } ?: run {
                emitter.resumeWithException(CustomError.SomethingWentWrong)
            }
        }

        override suspend fun getChatsByCreator() = suspendCoroutine { emitter ->
            FirebaseAuth.getInstance().currentUser?.also { user ->
                firestore
                    .collection(collectionChat)
                    .whereEqualTo("creator", FirebaseAuth.getInstance().currentUser?.uid)
                    .get()
                    .addOnSuccessListener {
                        scope.launch {
                            emitter.resume(it.documents.mapNotNull {
                                try {
                                    it.toObject(GroupChatDto::class.java)?.apply {
                                        if (opponents?.isEmpty() == true) {
                                            throw IllegalStateException()
                                        }
                                    }
                                } catch (e: Throwable) {
                                    it.toObject(ChatDto::class.java)?.apply {
                                        if (opponent?.isEmpty() == true) {
                                            throw IllegalStateException()
                                        }
                                    }
                                }
                            }.toCollection(ArrayList()))
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