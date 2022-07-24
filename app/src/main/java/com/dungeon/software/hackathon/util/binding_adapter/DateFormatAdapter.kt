package com.dungeon.software.hackathon.util.binding_adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.dungeon.software.hackathon.util.DateConverter
import com.dungeon.software.hackathon.util.ext.formatForChat
import java.text.DateFormat
import java.util.*

@BindingAdapter("app:setDate")
fun TextView.setDate(timestamp: Long) {
    text = DateConverter.longToUiDate(timestamp)
}