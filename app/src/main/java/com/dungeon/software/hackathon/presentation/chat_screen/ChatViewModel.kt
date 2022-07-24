package com.dungeon.software.hackathon.presentation.chat_screen

import android.net.Uri
import com.dungeon.software.hackathon.base.view_model.BaseViewModel
import com.dungeon.software.hackathon.data.data_source.StorageDataSource
import com.dungeon.software.hackathon.domain.models.Chat
import com.dungeon.software.hackathon.domain.models.Message
import com.dungeon.software.hackathon.domain.models.User
import com.dungeon.software.hackathon.domain.repository.ChatRepository
import com.dungeon.software.hackathon.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val storageDataSource: StorageDataSource,
) : BaseViewModel() {

    private val _currentUser: MutableStateFlow<User?> = MutableStateFlow(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _messageState: MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())
    val messageState: StateFlow<List<Message>> = _messageState

    init {
        getCurrentUser()
    }

    fun sendImage(uri: Uri, chatId: String, user: User) = launchRequest {
        sendMessage(
            Message(
                "",
                null,
                storageDataSource.saveFile(uri),
                null,
                System.currentTimeMillis(),
                false,
                user,
                null
            ), chatId
        )
    }

    fun sendMessage(message: Message, id: String) = launchRequest {
        chatRepository.sendMessage(message, id)
    }

    fun getCurrentUser() = launchRequest {
        _currentUser.emit(userRepository.fetchCurrentUser())
    }

    fun getChat(id: String) = launchRequest {
        chatRepository.getChat(id).collect {
            if (it is Chat) {
                _messageState.emit(it.messages)
            }
        }
    }
}