package com.dungeon.software.hackathon.base.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.dungeon.software.hackathon.R

class BaseProgressDialog(context: Context) : Dialog(context) {
    private var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle) {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.base_progress_dialog, null)
        textView = view.findViewById(R.id.messageText)
        setContentView(view)
        super.onCreate(savedInstanceState)
    }

    fun setMessage(message: String?) {
        textView?.visibility = View.VISIBLE
        textView?.text = message
    }

    override fun dismiss() {
        try {
            if (super.isShowing()) {
                if (context is Activity && (context as Activity).isFinishing) {
                    return
                }
                super.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun show() {
        if (context is Activity && (context as Activity).isFinishing || super.isShowing()) {
            return
        }
        try {
            super.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        fun show(context: Context, message: String?): BaseProgressDialog {
            val baseProgressDialog = BaseProgressDialog(context)
            baseProgressDialog.show()
            if (message != null) {
                baseProgressDialog.setMessage(message)
            }
            baseProgressDialog.setCancelable(false)
            return baseProgressDialog
        }

        fun show(context: Context): BaseProgressDialog {
            val baseProgressDialog = BaseProgressDialog(context)
            baseProgressDialog.show()
            baseProgressDialog.setCancelable(false)
            return baseProgressDialog
        }
    }
}
