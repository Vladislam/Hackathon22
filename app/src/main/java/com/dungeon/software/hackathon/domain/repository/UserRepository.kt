package com.dungeon.software.hackathon.domain.repository

import android.net.Uri
import com.dungeon.software.hackathon.data.data_source.StorageDataSource
import com.dungeon.software.hackathon.data.models.UserDto
import com.dungeon.software.hackathon.data.data_source.UserDataSource
import com.dungeon.software.hackathon.domain.models.User

interface UserRepository {

    suspend fun fetchListUsers(): List<User>

    suspend fun fetchCurrentUser(): User

    suspend fun fetchUser(id: String): User?

    suspend fun createUser(user: User)

    suspend fun addToFriends(user: User)

    suspend fun changeImage(url: Uri)

    suspend fun changeName(id: String, name: String)

    class Base(private val userDataSource: UserDataSource, private val storageDataSource: StorageDataSource) : UserRepository {

        override suspend fun fetchListUsers(): List<User> =
            userDataSource.fetchListUsers().map { User(it) }

        override suspend fun fetchCurrentUser(): User = User(userDataSource.fetchCurrentUser())

        override suspend fun fetchUser(id: String): User? =
            userDataSource.fetchUser(id)?.let { User(it) }

        override suspend fun createUser(user: User) = userDataSource.createUser(UserDto(user))

        override suspend fun addToFriends(user: User) = userDataSource.addToFriends(UserDto(user))

        override suspend fun changeImage(url: Uri) {
            userDataSource.changeImage(storageDataSource.saveFile(url))
        }

        override suspend fun changeName(id: String, name: String) = userDataSource.changeName(id, name)
    }
}