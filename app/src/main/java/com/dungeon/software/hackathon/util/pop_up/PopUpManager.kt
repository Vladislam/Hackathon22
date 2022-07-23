package com.dangeon.software.notes.util.pop_up

import android.content.Context
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.material.snackbar.Snackbar

class PopUpManager(private val context: Context) {

    private val defaultDuration = 3000
    private var view: View? = null
    private var lifecycle: Lifecycle? = null

    private var snackbar: Snackbar? = null

    private val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            destroy()
        }
    }

    fun attach(view: View, lifecycle: Lifecycle) {
        this.view = view
        this.lifecycle = lifecycle
        lifecycle.addObserver(observer)
    }

    fun showError(error: CustomError, duration: Int = defaultDuration) {
        view?.let { view ->
            error.getMessage(context)?.let {
                snackbar?.dismiss()
                Snackbar.make(view, it, duration).apply {
                    snackbar = this
                }.show()
            }
        }
    }

    fun dismiss() = snackbar?.dismiss()

    private fun destroy() {
        lifecycle?.removeObserver(observer)
        lifecycle = null
        view = null
        snackbar?.dismiss()
        snackbar = null
    }

}