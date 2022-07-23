package com.dungeon.software.hackathon.data.models

data class GroupChatDto(
    val message: List<MessageGroupDto>,
    val uid: String,
    val name: String,
    val opponents: List<String>,
    val imageUrl: String?
)