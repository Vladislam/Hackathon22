package com.dungeon.software.hackathon.domain.models

import android.os.Parcelable
import com.dungeon.software.hackathon.data.models.UserDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val uid: String,
    val name: String,
    val email: String,
    val imageUrl: String?,
    val friends: List<String>
) : Parcelable {

    constructor(user: UserDto): this(user.uid!!, user.name, user.email, user.imageUrl, user.friends)

}
