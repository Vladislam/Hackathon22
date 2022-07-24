package com.dungeon.software.hackathon.data.models

import com.dungeon.software.hackathon.domain.models.Message
import com.google.firebase.firestore.DocumentId

data class MessageDto(
    @DocumentId
    val uid: String? = null,
    val message: String? = null,
    val imageUrl: String? = null,
    val videoUrl: String? = null,
    val timeSent: Long = 0,
    val isSeen: Boolean = false,
    val userUid: String = "",
    val timeSeen: Long? = null
) : MessageDataDto {

    constructor(message: Message) : this(message.uid, message.message, message.imageUrl, message.videoUrl, message.timeSent, message.isSeen, message.user.uid, message.timeSeen)

}
