package com.dungeon.software.hackathon.data.models

import com.google.firebase.firestore.DocumentId

data class MessageGroupDto(
    @DocumentId
    val uid: String?,
    val message: String?,
    val imageUrl: String?,
    val videoUrl: String?,
    val timeSent: Long,
    val seen: List<String>,
    val userUid: String,
    val timeSeen: Map<String, Long>
): MessageDataDto