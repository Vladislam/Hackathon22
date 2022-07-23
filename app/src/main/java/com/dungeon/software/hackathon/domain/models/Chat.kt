package com.dungeon.software.hackathon.domain.models

import com.dungeon.software.hackathon.data.models.ChatDto

data class Chat(
    val messages: List<Message>,
    val uid: String,
    val opponent: User
): ChatData {

    constructor(messages: List<Message>, chat: ChatDto, opponent: User): this(messages, chat.uid!!, opponent)

}