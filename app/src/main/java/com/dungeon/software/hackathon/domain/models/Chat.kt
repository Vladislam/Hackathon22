package com.dungeon.software.hackathon.domain.models

data class Chat(
    val messages: List<Message>,
    val uid: String,
    val opponent: User
)