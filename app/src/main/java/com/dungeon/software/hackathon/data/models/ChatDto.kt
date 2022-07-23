package com.dungeon.software.hackathon.data.models

import com.google.firebase.firestore.DocumentId

data class ChatDto(
    @DocumentId
    val uid: String? = null,
    val opponent: String
): ChatDataDto
