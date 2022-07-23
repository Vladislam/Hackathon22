package com.dungeon.software.hackathon.data.data_source

import android.net.Uri
import com.dungeon.software.hackathon.data.models.UserDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


interface UserDataSource {

    suspend fun fetchListUsers(): List<UserDto>

    suspend fun fetchCurrentUser(): UserDto

    suspend fun fetchUser(id: String): UserDto?

    suspend fun addToFriends(user: UserDto)

    suspend fun changeImage(url: Uri): String

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

        override suspend fun changeImage(url: Uri): String = suspendCoroutine { continuation ->

            val storage: FirebaseStorage =
                FirebaseStorage.getInstance("gs://hackathon22-c0b43.appspot.com/")
            val basePath = "user_image"
            val userFolder = FirebaseAuth.getInstance().currentUser?.uid ?: ""

            val imageName = UUID.randomUUID().toString()
            val newImageReference: StorageReference = storage.reference.child(
                "$basePath/$userFolder/${imageName}"
            )
            val uploadTask = newImageReference.putFile(url)

            uploadTask.continueWithTask {
                if (!uploadTask.isSuccessful) {
                    uploadTask.exception?.let {
                        throw it
                    }
                }
                newImageReference.downloadUrl.addOnSuccessListener { uri ->
                    val url = uri.toString()
                    firestore.collection(USERS_COLLECTION)
                        .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                        .update(
                            mapOf("imageUrl" to url)
                        )
                        .addOnCompleteListener {
                            if (!it.isSuccessful) {
                                it.exception?.let {
                                    continuation.resumeWithException(it)
                                }
                            }
                        }
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(task.result.path ?: "")
                    } else {
                        task.exception?.let {
                            continuation.resumeWithException(it)
                        }
                    }
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
