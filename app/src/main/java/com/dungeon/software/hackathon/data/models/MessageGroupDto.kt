package com.dungeon.software.hackathon.data.models

import com.dungeon.software.hackathon.domain.models.MessageGroup
import com.google.firebase.firestore.DocumentId

data class MessageGroupDto(
    @DocumentId
    val uid: String? = null,
    val message: String? = null,
    val imageUrl: String? = null,
    val videoUrl: String? = null,
    val timeSent: Long = 0,
    val seen: List<String> = listOf(),
    val userUid: String = "",
    val timeSeen: Map<String, Long> = mapOf()
): MessageDataDto {

    constructor(message: MessageGroup): this(message.uid, message.message, message.imageUrl, message.videoUrl, message.timeSent, message.seen.map { it.uid }, message.user.uid, message.timeSeen)

}