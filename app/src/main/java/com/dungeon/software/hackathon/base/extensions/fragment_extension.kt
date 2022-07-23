package com.dungeon.software.hackathon.base.extensions

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    requireActivity().hideKeyboard()
}

fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = this.currentFocus
    if (view == null) view = View(this)
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}