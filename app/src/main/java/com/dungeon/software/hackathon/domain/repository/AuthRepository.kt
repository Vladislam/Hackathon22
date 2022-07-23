package com.dungeon.software.hackathon.domain.repository

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.dungeon.software.hackathon.data.models.UserDto
import com.dungeon.software.hackathon.data.repository.AuthDataSource

interface AuthRepository {

    suspend fun isLoggedIn(): Boolean

    suspend fun authWithIntent(intent: Intent): UserDto?

    suspend fun authWithGoogle(param: ActivityResultLauncher<Intent>)

    suspend fun logout()

    class Base(private val dataSource: AuthDataSource) : AuthRepository {

        override suspend fun isLoggedIn(): Boolean = dataSource.isLoggedIn()

        override suspend fun authWithIntent(intent: Intent): UserDto? = dataSource.authWithIntent(intent)

        override suspend fun authWithGoogle(param: ActivityResultLauncher<Intent>) = dataSource.authWithGoogle(param)

        override suspend fun logout() = dataSource.logout()
    }
}