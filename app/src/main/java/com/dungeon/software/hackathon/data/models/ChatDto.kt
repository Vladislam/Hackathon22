package com.dungeon.software.hackathon.data.models

import com.dungeon.software.hackathon.domain.models.Chat
import com.google.firebase.firestore.DocumentId

data class ChatDto(
    @DocumentId
    val uid: String? = null,
    val opponent: String = "",
    val creator: String = ""
) : ChatDataDto {

    constructor(chat: Chat, creator: String) : this(chat.uid, chat.opponent.uid, creator)

}
