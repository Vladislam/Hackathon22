package com.dungeon.software.hackathon.data.models

import com.dungeon.software.hackathon.domain.models.Chat
import com.google.firebase.firestore.DocumentId

data class ChatDto(
    @DocumentId
    val uid: String? = null,
    val opponent: String
) : ChatDataDto {

    constructor(chat: Chat) : this(chat.uid, chat.opponent.uid)

}
