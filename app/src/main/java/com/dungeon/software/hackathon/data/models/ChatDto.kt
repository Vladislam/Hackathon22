package com.dungeon.software.hackathon.data.models

data class ChatDto(
    val message: List<MessageDto>,
    val uid: String,
    val opponent: String
)
