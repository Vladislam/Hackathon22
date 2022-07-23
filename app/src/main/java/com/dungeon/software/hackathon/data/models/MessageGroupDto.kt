package com.dungeon.software.hackathon.data.models

data class MessageGroupDto(
    val uid: String,
    val message: List<String>,
    val imageUrl: String?,
    val videoUrl: String?,
    val timeSent: Long,
    val seen: List<String>,
    val userUid: String,
    val timeSeen: List<Long>
)