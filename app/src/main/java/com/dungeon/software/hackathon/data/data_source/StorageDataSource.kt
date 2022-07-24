package com.dungeon.software.hackathon.data.data_source

import android.net.Uri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
        val user = Firebase.auth.currentUser ?: run {
            emitter.resumeWithException(IllegalArgumentException())
            return@suspendCoroutine
        }

        val stream = FileInputStream(file)
        val ref = storageRef.child("user${user.uid}/file/${file.path}/${UUID.randomUUID()}")
        ref.putStream(stream)
            .addOnFailureListener {
                emitter.resumeWithException(it)
            }
            .addOnSuccessListener {
                ref.downloadUrl
                    .addOnSuccessListener {
                        emitter.resume(it.toString())
                    }
                    .addOnFailureListener {
                        emitter.resumeWithException(it)
                    }
            }
    }

    suspend fun saveFile(uri: Uri) = suspendCoroutine { emitter ->
        val user = Firebase.auth.currentUser ?: run {
            emitter.resumeWithException(IllegalArgumentException())
            return@suspendCoroutine
        }

        val ref = storageRef.child("user${user.uid}/file/${uri.path}/${UUID.randomUUID()}")
        ref.putFile(uri)
            .addOnFailureListener {
                emitter.resumeWithException(it)
            }
            .addOnSuccessListener {
                ref.downloadUrl
                    .addOnSuccessListener {
                        emitter.resume(it.toString())
                    }
                    .addOnFailureListener {
                        emitter.resumeWithException(it)
                    }
            }
    }

    suspend fun deleteFile(uri: String) = suspendCoroutine { continuation ->
        storage.getReferenceFromUrl(uri).delete()
            .addOnSuccessListener {
                continuation.resume(uri)
            }
            .addOnFailureListener {
                continuation.resumeWithException(it)
            }
    }


}