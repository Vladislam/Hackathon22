package com.dungeon.software.hackathon.domain.models

import android.os.Parcelable
import com.dungeon.software.hackathon.data.models.MessageGroupDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageGroup(
    val uid: String,
    val message: String?,
    val imageUrl: String?,
    val videoUrl: String?,
    val timeSent: Long,
    val seen: List<User>,
    val user: User,
    val timeSeen: Map<String, Long>
) : MessageData, Parcelable {
    override fun getMessageUid(): String = uid

    override fun getMessageText(): String? = message

    override fun getMessageImageUrl(): String? = imageUrl

    override fun getMessageVideoUrl(): String? = videoUrl

    override fun getMessageTimeSent(): Long = timeSent

    override fun getIsMessageSeen(): Boolean = seen.isNotEmpty()

    override fun getSender(): User = user

    override fun getMessageTimeSeen(): Long = timeSeen.values.min()

    constructor(message: MessageGroupDto, allUserInGroup: List<User>) : this(
        message.uid!!,
        message.message,
        message.imageUrl,
        message.videoUrl,
        message.timeSent,
        message.seen.mapNotNull { id -> allUserInGroup.find { id == it.uid } },
        allUserInGroup.find { message.userUid == it.uid }!!,
        message.timeSeen
    )

}
