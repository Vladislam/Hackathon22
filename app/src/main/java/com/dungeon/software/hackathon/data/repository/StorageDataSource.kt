package com.dungeon.software.hackathon.data.repository

import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileInputStream
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class StorageDataSource {

    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    suspend fun saveFile(file: File) = suspendCoroutine { emitter ->
        val user = "Firebase.auth.currentUser" ?: run {
            emitter.resumeWithException(IllegalArgumentException())
            return@suspendCoroutine
        }

        val stream = FileInputStream(file)
        val ref = storageRef.child("user${"user.uid"}/file/${file.path}/${UUID.randomUUID()}")
        ref.putStream(stream)
            .addOnFailureListener {
                emitter.resumeWithException(it)
            }
            .addOnSuccessListener { taskSnapshot ->
                ref.downloadUrl
                    .addOnSuccessListener {
                        emitter.resume(it.toString())
                    }
                    .addOnFailureListener {
                        emitter.resumeWithException(it)
                    }
            }
    }

}