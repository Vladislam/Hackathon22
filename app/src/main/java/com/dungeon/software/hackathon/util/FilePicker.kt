package com.dungeon.software.hackathon.util

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.dungeon.software.hackathon.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FilePicker(private val activity: AppCompatActivity) {

    private val resumer = PhotoResumer(activity)

    suspend fun getTakeImageFile() = resumer.takePicture()

    suspend fun getImageFile() = resumer.getImage()

    private class PhotoResumer(activity: AppCompatActivity) : Resumer(activity) {

        suspend fun takePicture(): File {
            getTakeImageFile()
            scope.cancel()
            return latestFile ?: throw NullPointerException()
        }

        suspend fun getImage(): Uri {
            getImageFile()
            scope.cancel()
            return latestUri ?: throw NullPointerException()
        }

        private suspend fun getTakeImageFile() = suspendCoroutine { emitter ->
            latestFile = getTmpFile().also {
                takeImageResult.launch(it.getUri())
                scope.launch {
                    successTaken.collect {
                        emitter.resume(it)
                    }
                }
            }
        }

        private suspend fun getImageFile() = suspendCoroutine { emitter ->
            activityResult.launch(Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
                activityResult.launch(this)
            })
            scope.launch {
                successTaken.collect {
                    emitter.resume(it)
                }
            }
        }

        private val activityResult =
            activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                scope.launch {
                    if (it.resultCode == RESULT_OK) {
                        it.data?.data?.let { imageUri ->
                            latestUri = imageUri
                            successPicked.emit(true)
                        }
                    }
                }
            }

        private val takeImageResult =
            activity.registerForActivityResult(ActivityResultContracts.TakePicture()) { _ ->
                scope.launch {
                    successTaken.emit(true)
                }
            }
        override val fileType = "png"

    }

    private abstract class Resumer(private val activity: AppCompatActivity) {

        abstract val fileType: String

        protected var latestFile: File? = null
        protected var latestUri: Uri? = null
        protected val successTaken = MutableSharedFlow<Boolean>()
        protected val successPicked = MutableSharedFlow<Boolean>()
        protected val scope = CoroutineScope(Dispatchers.IO)

        protected fun getTmpFile() =
            File.createTempFile("tmp_image_file", ".${fileType}", activity.cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }

        protected fun File.getUri() =
            FileProvider.getUriForFile(activity, "${BuildConfig.APPLICATION_ID}.provider", this)

    }

}