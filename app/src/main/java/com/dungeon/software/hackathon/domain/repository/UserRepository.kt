package com.dungeon.software.hackathon.domain.repository

import android.net.Uri
import com.dungeon.software.hackathon.data.data_source.StorageDataSource
import com.dungeon.software.hackathon.data.models.UserDto
import com.dungeon.software.hackathon.data.data_source.UserDataSource
import com.dungeon.software.hackathon.domain.models.User
import com.dungeon.software.hackathon.presentation.chats_list_screen.SortState
import com.dungeon.software.hackathon.presentation.chats_list_screen.SortType

interface UserRepository {

    suspend fun getUserList(sortState: SortState): List<User>

    suspend fun fetchCurrentUser(): User

    suspend fun fetchUser(id: String): User?

    suspend fun createUser(user: User)

    suspend fun addToFriends(user: User)

    suspend fun changeImage(url: Uri)

    suspend fun changeName(id: String, name: String)

    class Base(private val userDataSource: UserDataSource, private val storageDataSource: StorageDataSource) : UserRepository {

        override suspend fun getUserList(sortState: SortState): List<User> {
            val type = sortState.sortType
            return when (type) {
                is SortType.Name -> {
                    userDataSource.getListUsersByName(sortState.query, !type.desc)
                }
                is SortType.LastMessageTime -> {
                    userDataSource.getListUsersByEmail(sortState.query, !type.desc)
                }
            }.toSet().map {
                User(it)
            }
        }

        override suspend fun fetchCurrentUser(): User = User(userDataSource.getCurrentUser())

        override suspend fun fetchUser(id: String): User? =
            userDataSource.getUser(id)?.let { User(it) }

        override suspend fun createUser(user: User) = userDataSource.createUser(UserDto(user))

        override suspend fun addToFriends(user: User) = userDataSource.addToFriends(UserDto(user))

        override suspend fun changeImage(url: Uri) {
            userDataSource.changeImage(storageDataSource.saveFile(url))
        }

        override suspend fun changeName(id: String, name: String) = userDataSource.changeName(id, name)
    }
}