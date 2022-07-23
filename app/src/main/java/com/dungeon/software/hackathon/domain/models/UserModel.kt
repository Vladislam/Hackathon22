package com.dungeon.software.hackathon.domain.models

import com.dungeon.software.hackathon.data.models.User

data class UserModel(
    val uid: String,
    val name: String,
    val email:String,
    val imageUrl: String?,
    val friends: List<User>
)
