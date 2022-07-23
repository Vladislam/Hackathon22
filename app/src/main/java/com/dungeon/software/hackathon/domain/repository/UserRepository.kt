package com.dungeon.software.hackathon.domain.repository

import android.net.Uri
import com.dungeon.software.hackathon.data.models.UserDto
import com.dungeon.software.hackathon.data.data_source.UserDataSource
import com.dungeon.software.hackathon.domain.models.User

interface UserRepository {

    suspend fun fetchListUsers(): List<User>

    suspend fun fetchCurrentUser(): User

    suspend fun fetchUser(id: String): User?

    suspend fun createUser(user: User)

    suspend fun addToFriends(user: User)

    suspend fun changeImage(url: Uri): String

    suspend fun changeName(id: String, name: String)

    class Base(private val dataSource: UserDataSource) : UserRepository {

        override suspend fun fetchListUsers(): List<User> =
            dataSource.fetchListUsers().map { User(it) }

        override suspend fun fetchCurrentUser(): User = User(dataSource.fetchCurrentUser())

        override suspend fun fetchUser(id: String): User? =
            dataSource.fetchUser(id)?.let { User(it) }

        override suspend fun createUser(user: User) = dataSource.createUser(UserDto(user))

        override suspend fun addToFriends(user: User) = dataSource.addToFriends(UserDto(user))

        override suspend fun changeImage(url: Uri): String = dataSource.changeImage(url)

        override suspend fun changeName(id: String, name: String) = dataSource.changeName(id, name)
    }
}