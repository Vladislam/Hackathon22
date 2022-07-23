package com.dungeon.software.hackathon.util

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

    suspend fun getImageFile() = PhotoResumer(activity).getImage()

    private class PhotoResumer(activity: AppCompatActivity) : Resumer(activity) {

        suspend fun getImage(): File {
            getImageFile()
            scope.cancel()
            return latestFile ?: throw NullPointerException()
        }

        private suspend fun getImageFile() = suspendCoroutine { emitter ->
            latestFile = getTmpFile().also {
                takeImageResult.launch(it.getUri())
                scope.launch {
                    successPicked.collect {
                        emitter.resume(it)
                    }
                }
            }
        }

        private val takeImageResult =
            activity.registerForActivityResult(ActivityResultContracts.TakePicture()) { _ ->
                scope.launch {
                    successPicked.emit(true)
                }
            }
        override val fileType = "png"

    }

    private abstract class Resumer(private val activity: AppCompatActivity) {

        abstract val fileType: String

        protected var latestFile: File? = null
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