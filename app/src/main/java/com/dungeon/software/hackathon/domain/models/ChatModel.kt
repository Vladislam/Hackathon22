package com.dungeon.software.hackathon.domain.models

data class ChatModel(
    val messages: List<MessageModel>,
    val uid: String,
    val opponent: UserModel
)