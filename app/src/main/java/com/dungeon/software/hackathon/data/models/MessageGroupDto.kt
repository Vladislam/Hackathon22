package com.dungeon.software.hackathon.data.models

import com.dungeon.software.hackathon.domain.models.MessageGroup
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
): MessageDataDto {

    constructor(message: MessageGroup): this(message.uid, message.message, message.imageUrl, message.videoUrl, message.timeSent, message.seen.map { it.uid }, message.user.uid, message.timeSeen)

}