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

    suspend fun getImageFile() = Resumer(activity).getImage()

    private class Resumer(private val activity: AppCompatActivity) {

        private val scope = CoroutineScope(Dispatchers.IO)

        private var latestFile: File? = null
        private val successPicked = MutableSharedFlow<Boolean>()

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

        private val takeImageResult = activity.registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            scope.launch {
                successPicked.emit(true)
            }
        }

        private fun getTmpFile() = File.createTempFile("tmp_image_file", ".png", activity.cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        private fun File.getUri() = FileProvider.getUriForFile(activity, "${BuildConfig.APPLICATION_ID}.provider", this)

    }

}