package com.dungeon.software.hackathon.data.models

data class Message(
    val uid: String,
    val message: String?,
    val imageUrl: String?,
    val videoUrl: String?,
    val timeSent: Long,
    val isSeen: Boolean,
    val userUid: String,
    val timeSeen: Long?
)
