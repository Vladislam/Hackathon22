package com.dungeon.software.hackathon.data.repository

import com.dungeon.software.hackathon.data.models.UserDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface UserDataSource {

    suspend fun fetchListUsers(): List<UserDto>

    suspend fun fetchUser(id: String) : UserDto?

    suspend fun createUser(user: UserDto)

    suspend fun addToFriends(user: UserDto)

    suspend fun changeImage(url: String)

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

        override suspend fun fetchUser(id: String) : UserDto? = suspendCoroutine { continuation ->
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

        override suspend fun createUser(user: UserDto) {
            firestore.collection(USERS_COLLECTION)
                .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                .set(user)
                .await()
        }

        override suspend fun addToFriends(user: UserDto) {
            firestore.collection(USERS_COLLECTION)
                .document()
                .update("friends", user.friends)
                .await()
        }

        override suspend fun changeImage(url: String) {
            firestore.collection(USERS_COLLECTION)
                .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                .update(
                    mapOf("imageUrl" to url)
                )
                .await()
        }

        override suspend fun changeName(id: String, name: String) {
            firestore.collection(USERS_COLLECTION)
                .document(id)
                .update("name", name)
                .await()
        }

        companion object {
            private const val USERS_COLLECTION = "users"
        }
    }
}