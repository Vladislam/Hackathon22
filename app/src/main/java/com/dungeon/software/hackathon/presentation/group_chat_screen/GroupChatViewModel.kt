package com.dungeon.software.hackathon.presentation.group_chat_screen

import com.dungeon.software.hackathon.base.view_model.BaseViewModel
import com.dungeon.software.hackathon.domain.models.GroupChat
import com.dungeon.software.hackathon.domain.models.Message
import com.dungeon.software.hackathon.domain.models.MessageGroup
import com.dungeon.software.hackathon.domain.models.User
import com.dungeon.software.hackathon.domain.repository.ChatRepository
import com.dungeon.software.hackathon.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GroupChatViewModel(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    private val _currentUser: MutableStateFlow<User?> = MutableStateFlow(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _messageState: MutableStateFlow<List<MessageGroup>> = MutableStateFlow(emptyList())
    val messageState: StateFlow<List<MessageGroup>> = _messageState

    init {
        getCurrentUser()
    }

    fun sendMessage(message: MessageGroup, id: String) = launchRequest {
        chatRepository.sendMessage(message, id)
    }

    fun getCurrentUser() = launchRequest {
        _currentUser.emit(userRepository.fetchCurrentUser())
    }

    fun getChat(id: String) = launchRequest {
        chatRepository.getChat(id).collect {
            if (it is GroupChat) {
                _messageState.emit(it.messages)
            }
        }
    }
}