package com.dungeon.software.hackathon.util.binding_adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.dungeon.software.hackathon.util.ext.formatForChat
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

@BindingAdapter("app:setDate")
fun TextView.setDate(timestamp: Long) {
    val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
    text = dateTime.formatForChat(context)
}