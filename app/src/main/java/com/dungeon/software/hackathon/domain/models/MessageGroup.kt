package com.dungeon.software.hackathon.domain.models

data class MessageGroup(
    val uid: String,
    val message: String?,
    val imageUrl: String?,
    val videoUrl: String?,
    val timeSent: String,
    val seen: List<User>,
    val userUid: String,
    val timeSeen: Map<String, Long>
)
