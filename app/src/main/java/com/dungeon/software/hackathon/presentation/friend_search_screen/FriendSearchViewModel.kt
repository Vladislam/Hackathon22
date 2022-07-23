package com.dungeon.software.hackathon.presentation.friend_search_screen

import com.dungeon.software.hackathon.base.view_model.BaseViewModel
import com.dungeon.software.hackathon.domain.models.ChatData
import com.dungeon.software.hackathon.domain.models.User
import com.dungeon.software.hackathon.domain.repository.ChatRepository
import com.dungeon.software.hackathon.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FriendSearchViewModel(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    private val _currentUser: MutableStateFlow<User?> = MutableStateFlow(null)
    val currentUser: StateFlow<User?> = _currentUser

    init {
        getCurrentUser()
    }

    fun createChat(chat: ChatData) = launchRequest {
        chatRepository.createChat(chat)
    }

    private fun getCurrentUser() = launchRequest {
        _currentUser.emit(userRepository.fetchCurrentUser())
    }
}