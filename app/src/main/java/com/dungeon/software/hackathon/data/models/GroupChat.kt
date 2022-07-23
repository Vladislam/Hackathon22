package com.dungeon.software.hackathon.data.models

data class GroupChat(
    val message: List<MessageGroup>,
    val uid: String,
    val name: String,
    val opponents: List<String>,
    val imageUrl: String?
)