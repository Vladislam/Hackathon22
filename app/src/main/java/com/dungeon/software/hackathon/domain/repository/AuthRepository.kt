package com.dungeon.software.hackathon.domain.repository

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.dungeon.software.hackathon.data.data_source.AuthDataSource
import com.dungeon.software.hackathon.domain.models.User

interface AuthRepository {

    suspend fun isLoggedIn(): Boolean

    suspend fun authWithIntent(intent: Intent): User?

    suspend fun authWithGoogle(param: ActivityResultLauncher<Intent>)

    suspend fun checkUser(): Boolean

    suspend fun logout()

    class Base(private val dataSource: AuthDataSource) : AuthRepository {

        override suspend fun isLoggedIn(): Boolean = dataSource.isLoggedIn()

        override suspend fun authWithIntent(intent: Intent): User? =
            dataSource.authWithIntent(intent)?.let {
                User(it)
            }

        override suspend fun authWithGoogle(param: ActivityResultLauncher<Intent>) =
            dataSource.authWithGoogle(param)

        override suspend fun checkUser(): Boolean = dataSource.checkUser()

        override suspend fun logout() = dataSource.logout()
    }
}