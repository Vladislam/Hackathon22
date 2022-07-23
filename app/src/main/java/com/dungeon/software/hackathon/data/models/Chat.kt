package com.dungeon.software.hackathon.data.models

data class Chat(
    val message: List<Message>,
    val uid: String,
    val opponent: String
)
