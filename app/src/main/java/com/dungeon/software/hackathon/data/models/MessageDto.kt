package com.dungeon.software.hackathon.data.models

import com.dungeon.software.hackathon.domain.models.Message
import com.google.firebase.firestore.DocumentId

data class MessageDto(
    @DocumentId
    val uid: String?,
    val message: String?,
    val imageUrl: String?,
    val videoUrl: String?,
    val timeSent: Long,
    val isSeen: Boolean,
    val userUid: String,
    val timeSeen: Long?
) : MessageDataDto {

    constructor(message: Message) : this(message.uid, message.message, message.imageUrl, message.videoUrl, message.timeSent, message.isSeen, message.user.uid, message.timeSeen)

}
