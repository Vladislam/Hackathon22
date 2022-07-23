package com.dungeon.software.hackathon.domain.models

data class MessageGroup(
    val uid: String,
    val message: String?,
    val imageUrl: String?,
    val videoUrl: String?,
    val timeSent: Long,
    val seen: List<User>,
    val user: User,
    val timeSeen: Map<String, Long>
) : MessageData {
    override fun getMessageUid(): String = uid

    override fun getMessageText(): String? = message

    override fun getMessageImageUrl(): String? = imageUrl

    override fun getMessageVideoUrl(): String? = videoUrl

    override fun getMessageTimeSent(): Long = timeSent

    override fun getIsMessageSeen(): Boolean = seen.isNotEmpty()

    override fun getSender(): User = user

    override fun getMessageTimeSeen(): Long = timeSeen.values.min()
}
