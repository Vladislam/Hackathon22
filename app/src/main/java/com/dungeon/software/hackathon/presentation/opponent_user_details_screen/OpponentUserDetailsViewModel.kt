package com.dungeon.software.hackathon.presentation.opponent_user_details_screen

import com.dungeon.software.hackathon.base.view_model.BaseViewModel
import com.dungeon.software.hackathon.domain.models.User
import com.dungeon.software.hackathon.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OpponentUserDetailsViewModel(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _friendsState: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())
    val friendsState: StateFlow<List<User>> = _friendsState

    fun getFriends(friendsUid: List<String>) = launchRequest {
        val friends: MutableList<User> = mutableListOf()
        friendsUid.forEach {
            userRepository.fetchUser(it)?.let { user ->
                friends.add(user)
            }
        }
        _friendsState.emit(friends)
    }
}