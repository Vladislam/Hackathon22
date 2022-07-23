package com.dungeon.software.hackathon.data.models

import com.google.firebase.firestore.DocumentId

data class GroupChatDto(
    val message: List<MessageGroupDto> = listOf(),
    @DocumentId
    val uid: String? = null,
    val name: String = "",
    val opponents: List<String> = listOf(),
    val imageUrl: String? = null
): ChatDataDto