package com.dungeon.software.hackathon.domain.models

import android.os.Parcelable
import com.dungeon.software.hackathon.data.models.ChatDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat(
    val messages: List<Message>,
    val uid: String,
    val opponent: User
) : ChatData, Parcelable {

    constructor(messages: List<Message>, chat: ChatDto, opponent: User) : this(
        messages,
        chat.uid!!,
        opponent
    )

}