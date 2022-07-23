package com.dungeon.software.hackathon.domain.models

data class MessageGroupModel(
    val uid: String,
    val message: String?,
    val imageUrl: String?,
    val videoUrl: String?,
    val timeSent: String,
    val seen: List<UserModel>,
    val userUid: String,
    val timeSeen: Map<String, Long>
)
