package com.dungeon.software.hackathon.data.models

import com.google.firebase.firestore.DocumentId

data class UserDto(
    @DocumentId
    val uid: String? = null,
    val name: String = "",
    val email: String = "",
    val imageUrl: String? = null,
    val friends: List<String> = listOf()
)