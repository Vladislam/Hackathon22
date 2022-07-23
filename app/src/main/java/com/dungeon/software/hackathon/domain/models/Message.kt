package com.dungeon.software.hackathon.domain.models

data class Message(
    val uid: String,
    val message: String?,
    val imageUrl: String?,
    val videoUrl: String?,
    val timeSent: Long,
    val isSeen: Boolean,
    val user: User,
    val timeSeen: Long?
)