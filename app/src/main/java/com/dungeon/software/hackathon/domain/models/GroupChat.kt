package com.dungeon.software.hackathon.domain.models

import android.os.Parcelable
import com.dungeon.software.hackathon.data.models.GroupChatDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupChat(
    val message: List<MessageGroup>,
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

}