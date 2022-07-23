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
) : MessageData {
    override fun getMessageUid(): String = uid

    override fun getMessageText(): String? = message

    override fun getMessageImageUrl(): String? = imageUrl

    override fun getMessageVideoUrl(): String? = videoUrl

    override fun getMessageTimeSent(): Long = timeSent

    override fun getIsMessageSeen(): Boolean = isSeen

    override fun getSender(): User = user

    override fun getMessageTimeSeen(): Long? = timeSeen
}