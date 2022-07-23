package com.dungeon.software.hackathon.data.models

data class User(
    val uid: String,
    val name: String,
    val email: String,
    val imageUrl: String?,
    val friends: List<String>
)