package com.dungeon.software.hackathon.util.ext

import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener

fun View.updateVisibility(visible: Boolean) {
    if (visible) this.visibility = View.VISIBLE else this.visibility = View.GONE
}

inline fun EditText.onTextChange(crossinline listener: (String) -> Unit) {
    addTextChangedListener(
        onTextChanged = { text, _, _, _ ->
            listener.invoke(text?.toString().orEmpty())
        }
    )
}