package com.dungeon.software.hackathon.presentation.chats_list_screen

import androidx.lifecycle.viewModelScope
import com.dungeon.software.hackathon.base.view_model.BaseViewModel
import com.dungeon.software.hackathon.domain.models.ChatData
import com.dungeon.software.hackathon.domain.repository.ChatRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalCoroutinesApi
class ChatsListViewModel(
    private val chatRepository: ChatRepository
) : BaseViewModel() {

    private var sortState: StateFlow<SortState>? = null
    private var sortStateListenerJob: Job? = null

    private val _chatsState: MutableStateFlow<List<ChatData>> = MutableStateFlow(emptyList())
    val chatsState: StateFlow<List<ChatData>> = _chatsState

    fun initSortState(sortState: StateFlow<SortState>) {
        this.sortState = sortState
        listen()
    }

    private fun listen() {
        sortStateListenerJob?.cancel()
        sortStateListenerJob = viewModelScope.launch {
            kotlin.runCatching {
                sortState?.flatMapLatest { sortState ->
                    chatRepository.getChats().map { chats ->
                        when (sortState.sortType) {
                            is SortType.LastMessageTime -> {
                                if (sortState.sortType.desc) {
                                    chats.sortedByDescending {
                                        it.getChatLastMessageTime()
                                    }
                                } else {
                                    chats.sortedBy {
                                        it.getChatLastMessageTime()
                                    }
                                }
                            }
                            is SortType.Name -> {
                                if (sortState.sortType.desc) {
                                    chats.sortedByDescending {
                                        it.getChatName()
                                    }
                                } else {
                                    chats.sortedBy {
                                        it.getChatName()
                                    }
                                }
                            }
                        }
                    }
                }
            }.onSuccess { flow ->
                flow?.collect {
                    _chatsState.emit(it)
                }
            }.onFailure {
                _errorMessage.emit(it.message ?: "Unexpected error")
                Timber.e(it)
            }
        }
    }

    fun disposeChatsListeners() {
        chatRepository.disposeChatsListeners()
    }
}