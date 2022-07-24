package com.dungeon.software.hackathon.util.binding_adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.dungeon.software.hackathon.util.ext.formatForChat
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@BindingAdapter("app:setDate")
fun TextView.setDate(timestamp: Long) = kotlin.runCatching {
    val dateTime =
        LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timestamp),
            TimeZone.getDefault().toZoneId()
        )
    text = dateTime.formatForChat(context)
}