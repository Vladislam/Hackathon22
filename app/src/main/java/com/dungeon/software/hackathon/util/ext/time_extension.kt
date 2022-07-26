package com.dungeon.software.hackathon.util.ext

import android.content.Context
import android.text.format.DateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun LocalDateTime.formatForChat(context: Context): String {
    val now = LocalDateTime.now()
    return if (now.toLocalDate().isEqual(toLocalDate())) {
        DateTimeFormatter.ofPattern(
            if (DateFormat.is24HourFormat(context)) {
                "H:mm"
            } else {
                "h:mm a"
            }
        ).format(this)
    } else if (ChronoUnit.WEEKS.between(this, now) == 0L) {
        DateTimeFormatter.ofPattern("EEE").format(this)
    } else if (ChronoUnit.WEEKS.between(this, now) > 0L && year == now.year) {
        DateTimeFormatter.ofPattern("ccc dd").format(this)
    } else {
        DateTimeFormatter.ofPattern("yyyy ccc dd").format(this)
    }
}