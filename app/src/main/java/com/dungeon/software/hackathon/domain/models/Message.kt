package com.dungeon.software.hackathon.domain.models

import android.os.Parcelable
import com.dungeon.software.hackathon.data.models.MessageDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(
    val uid: String,
    val message: String?,
    val imageUrl: String?,
    val videoUrl: String?,
    val timeSent: Long,
    val isSeen: Boolean,
    val user: User,
    val timeSeen: Long?
) : MessageData, Parcelable {

    override fun getMessageUid(): String = uid

    override fun getMessageText(): String? = message

    override fun getMessageImageUrl(): String? = imageUrl

    override fun getMessageVideoUrl(): String? = videoUrl

    override fun getMessageTimeSent(): Long = timeSent

    override fun getIsMessageSeen(): Boolean = isSeen

    override fun getSender(): User = user

    override fun getMessageTimeSeen(): Long? = timeSeen

    constructor(message: MessageDto, user: User) : this(
        message.uid!!,
        message.message,
        message.imageUrl,
        message.videoUrl,
        message.timeSent,
        message.isSeen,
        user,
        message.timeSeen
    )

}