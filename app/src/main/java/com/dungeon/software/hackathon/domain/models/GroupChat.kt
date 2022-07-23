package com.dungeon.software.hackathon.domain.models

import com.dungeon.software.hackathon.data.models.GroupChatDto

data class GroupChat(
    val message: List<MessageGroup>,
    val uid: String,
    val name: String,
    val opponents: List<User>,
    val imageUrl: String?
) : ChatData {

    constructor(messages: List<MessageGroup>, chat: GroupChatDto, users: List<User>) : this(messages, chat.uid!!, chat.name, users, chat.imageUrl)

}