package com.dungeon.software.hackathon.domain.models

import com.dungeon.software.hackathon.data.models.UserDto

data class User(
    val uid: String,
    val name: String,
    val email:String,
    val imageUrl: String?,
    val friends: List<UserDto>
)
