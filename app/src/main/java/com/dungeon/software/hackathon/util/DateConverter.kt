package com.dungeon.software.hackathon.util

import java.text.SimpleDateFormat
import java.util.*

object DateConverter {

    private const val defaultPattern = "HH:mm dd-MM-yyyy"

    fun longToUiDate(long: Long): String = kotlin.runCatching {
        SimpleDateFormat(defaultPattern, Locale.getDefault()).format(Date(long))
    }.getOrNull() ?: String()

}