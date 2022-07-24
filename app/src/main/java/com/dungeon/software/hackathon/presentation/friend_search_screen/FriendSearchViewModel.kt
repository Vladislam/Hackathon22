package com.dungeon.software.hackathon.presentation.friend_search_screen

import androidx.lifecycle.viewModelScope
import com.dangeon.software.notes.util.pop_up.CustomError
import com.dungeon.software.hackathon.base.view_model.BaseViewModel
import com.dungeon.software.hackathon.domain.models.ChatData
import com.dungeon.software.hackathon.domain.models.User
import com.dungeon.software.hackathon.domain.repository.ChatRepository
import com.dungeon.software.hackathon.domain.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FriendSearchViewModel(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    private val _currentUser: MutableStateFlow<User?> = MutableStateFlow(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _chatCreated: MutableSharedFlow<ChatData> = MutableSharedFlow()
    val chatCreated: SharedFlow<ChatData> = _chatCreated

    private val _friends: MutableSharedFlow<List<User>> = MutableSharedFlow()
    val friends = _friends.asSharedFlow()

    private var searchJob: Job? = null

    init {
        getCurrentUser()
        searchUser("")
    }

    fun createChat(chat: ChatData) = launchRequest {
        chatRepository.createChat(chat)
        _chatCreated.emit(chat)
    }

    private fun getCurrentUser() = launchRequest {
        _currentUser.emit(userRepository.fetchCurrentUser())
    }

    fun addToFriends(user: User) {
        launchRequest {
            userRepository.addToFriends(user)
        }
    }

    fun searchUser(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            kotlin.runCatching {
                if (query.isEmpty()) {
                    _friends.emit(userRepository.getFriends())
                } else {
                    _friends.emit(userRepository.getUserList(query))
                }
            }.onFailure {
                _error.send(CustomError.parse(it))
            }
        }
    }

}