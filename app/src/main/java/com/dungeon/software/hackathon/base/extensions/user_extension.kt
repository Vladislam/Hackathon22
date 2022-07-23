package com.dungeon.software.hackathon.base.extensions

import com.dungeon.software.hackathon.data.models.UserDto
import com.dungeon.software.hackathon.domain.models.User
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

fun GoogleSignInAccount?.toUserDto() = this?.run {
    UserDto(
        uid = this.id ?: "-",
        name = this.displayName ?: "-",
        email = this.email ?: "-",
        imageUrl = this.photoUrl.toString(),
        friends = listOf()
    )
}