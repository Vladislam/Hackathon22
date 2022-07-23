package com.dungeon.software.hackathon.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val uid: String,
    val name: String,
    val email: String,
    val imageUrl: String?,
    val friends: List<User>
) : Parcelable
