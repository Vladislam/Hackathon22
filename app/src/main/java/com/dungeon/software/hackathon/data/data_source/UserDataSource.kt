package com.dungeon.software.hackathon.data.data_source

import com.dangeon.software.notes.util.pop_up.CustomError
import com.dungeon.software.hackathon.data.models.UserDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


interface UserDataSource {

    suspend fun fetchListUsers(): List<UserDto>

    suspend fun fetchCurrentUser(): UserDto

    suspend fun fetchUser(id: String): UserDto?

    suspend fun addToFriends(user: UserDto)

    suspend fun changeImage(url: String)

    suspend fun createUser(user: UserDto)

    suspend fun changeName(id: String, name: String)

    class Base(private val firestore: FirebaseFirestore) : UserDataSource {

        override suspend fun fetchListUsers(): List<UserDto> = suspendCoroutine { continuation ->
            firestore.collection(USERS_COLLECTION)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    continuation.resume(querySnapshot.toObjects(UserDto::class.java))
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

        override suspend fun fetchCurrentUser(): UserDto = suspendCoroutine { continuation ->
            firestore.collection(USERS_COLLECTION)
                .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                .get()
                .addOnSuccessListener { snapShot ->
                    snapShot.toObject(UserDto::class.java)?.let {
                        continuation.resume(it)
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

        override suspend fun fetchUser(id: String): UserDto? = suspendCoroutine { continuation ->
            firestore.collection(USERS_COLLECTION)
                .document(id)
                .get()
                .addOnSuccessListener { snapshot ->
                    continuation.resume(snapshot.toObject(UserDto::class.java))
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }


        override suspend fun addToFriends(user: UserDto) = suspendCoroutine { continuation ->
            firestore.collection(USERS_COLLECTION)
                .document()
                .update("friends", user.friends)
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }

        override suspend fun changeImage(url: String) = suspendCoroutine { continuation ->
            firestore.collection(USERS_COLLECTION)
                .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                .update(mapOf("imageUrl" to url))
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        continuation.resumeWithException(it.exception ?: CustomError.SomethingWentWrong)
                    } else {
                        continuation.resume(Unit)
                    }
                }
        }

        override suspend fun changeName(id: String, name: String) =
            suspendCancellableCoroutine { emitter ->
                firestore.collection(USERS_COLLECTION)
                    .document(id)
                    .update("name", name)
                    .addOnSuccessListener {
                        emitter.resume(Unit)
                    }
                    .addOnFailureListener {
                        emitter.resumeWithException(it)
                    }
            }

        override suspend fun createUser(user: UserDto) = suspendCoroutine { continuation ->
            val currentUser = FirebaseAuth.getInstance().currentUser?.uid ?: kotlin.run {
                continuation.resumeWithException(NullPointerException())
                return@suspendCoroutine
            }
            firestore.collection(USERS_COLLECTION)
                .document(currentUser)
                .set(user)
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    companion object {
        const val USERS_COLLECTION = "users"
    }
}
