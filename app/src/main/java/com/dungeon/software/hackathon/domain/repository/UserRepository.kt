package com.dungeon.software.hackathon.domain.repository

import android.net.Uri
import com.dungeon.software.hackathon.data.models.UserDto
import com.dungeon.software.hackathon.data.repository.UserDataSource

interface UserRepository {

    suspend fun fetchListUsers(): List<UserDto>

    suspend fun fetchCurrentUser() : UserDto

    suspend fun fetchUser(id: String) : UserDto?

    suspend fun createUser(user: UserDto)

    suspend fun addToFriends(user: UserDto)

    suspend fun changeImage(url: Uri) : String

    suspend fun changeName(id: String, name: String)

    class Base(private val dataSource: UserDataSource) : UserRepository {

        override suspend fun fetchListUsers(): List<UserDto> = dataSource.fetchListUsers()

        override suspend fun fetchCurrentUser(): UserDto = dataSource.fetchCurrentUser()

        override suspend fun fetchUser(id: String): UserDto? = dataSource.fetchUser(id)

        override suspend fun createUser(user: UserDto) = dataSource.createUser(user)

        override suspend fun addToFriends(user: UserDto) = dataSource.addToFriends(user)

        override suspend fun changeImage(url: Uri) : String = dataSource.changeImage(url)

        override suspend fun changeName(id: String, name: String) = dataSource.changeName(id, name)

    }
}