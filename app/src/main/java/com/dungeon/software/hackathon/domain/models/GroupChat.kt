package com.dungeon.software.hackathon.domain.models

import android.os.Parcelable
import com.dungeon.software.hackathon.data.models.GroupChatDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupChat(
    val messages: List<MessageGroup>,
    val uid: String,
    val name: String,
    val opponents: List<User>,
    val imageUrl: String?
) : ChatData, Parcelable {

    constructor(
        messages: List<MessageGroup>,
        chat: GroupChatDto,
        users: List<User>
    ) : this(messages, chat.uid!!, chat.name, users, chat.imageUrl)

    override fun getChatImageUrl(): String? = imageUrl

    override fun getChatName(): String = name

    override fun getChatLastMessageTime(): Long =
        messages.lastOrNull()?.timeSeen?.values?.min() ?: 0

    override fun getChatLastMessage(): String = messages.lastOrNull()?.message.orEmpty()

}