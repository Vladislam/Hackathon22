package com.dungeon.software.hackathon.domain.repository

import android.net.Uri
import com.dungeon.software.hackathon.data.data_source.StorageDataSource
import com.dungeon.software.hackathon.data.data_source.UserDataSource
import com.dungeon.software.hackathon.data.models.UserDto
import com.dungeon.software.hackathon.domain.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

interface UserRepository {

    suspend fun getUserList(query: String): List<User>

    suspend fun fetchCurrentUser(): User

    suspend fun fetchUser(id: String): User?

    suspend fun createUser(user: User)

    suspend fun addToFriends(user: User)

    suspend fun changeImage(url: Uri)

    suspend fun deleteFile(url: String) : String

    suspend fun changeName(name: String)

    suspend fun getFriends(): List<User>

    class Base(private val userDataSource: UserDataSource, private val storageDataSource: StorageDataSource) : UserRepository {

        private val scope = CoroutineScope(Dispatchers.IO)

        override suspend fun getUserList(query: String): List<User> {
            val userUid = FirebaseAuth.getInstance().currentUser?.uid
            val byName = scope.async { userDataSource.getListUsersByName(query) }
            val byEmail = scope.async { userDataSource.getListUsersByEmail(query) }

            return byName.await().toCollection(ArrayList()).apply { addAll(byEmail.await()) }.toSet().map { User(it) }.filter { userUid != it.uid }
        }

        override suspend fun fetchCurrentUser(): User = User(userDataSource.getCurrentUser())

        override suspend fun fetchUser(id: String): User? =
            userDataSource.getUser(id)?.let { User(it) }

        override suspend fun createUser(user: User) = userDataSource.createUser(UserDto(user))

        override suspend fun addToFriends(user: User) = userDataSource.addToFriends(UserDto(user))

        override suspend fun changeImage(url: Uri) {
            userDataSource.changeImage(storageDataSource.saveFile(url))
        }

        override suspend fun deleteFile(url: String) : String = storageDataSource.deleteFile(url)

        override suspend fun changeName(name: String) = userDataSource.changeName(name)

        override suspend fun getFriends(): List<User> =
            userDataSource.getCurrentUser().friends.map { scope.async { userDataSource.getUser(it) } }.awaitAll().mapNotNull { it }.map { User(it) }

    }
}