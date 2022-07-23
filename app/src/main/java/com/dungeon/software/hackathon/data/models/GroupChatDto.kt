package com.dungeon.software.hackathon.data.models

import com.dungeon.software.hackathon.domain.models.GroupChat
import com.google.firebase.firestore.DocumentId

data class GroupChatDto(
    @DocumentId
    val uid: String? = null,
    val name: String = "",
    val opponents: List<String> = listOf(),
    val imageUrl: String? = null
) : ChatDataDto {

    constructor(chat: GroupChat) : this(chat.uid, chat.name, chat.opponents.map { it.uid }, chat.imageUrl)

}