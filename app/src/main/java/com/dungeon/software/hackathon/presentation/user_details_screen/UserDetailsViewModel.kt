package com.dungeon.software.hackathon.presentation.user_details_screen

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.dungeon.software.hackathon.base.view_model.BaseViewModel
import com.dungeon.software.hackathon.data.models.UserDto
import com.dungeon.software.hackathon.domain.repository.AuthRepository
import com.dungeon.software.hackathon.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class UserDetailsViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _currentUser = MutableSharedFlow<UserDto>()
    val currentUser: SharedFlow<UserDto> = _currentUser

    private val _logout = MutableSharedFlow<Boolean>()
    val logout: SharedFlow<Boolean> = _logout

    init {
        fetchUser()
    }

    fun updateImageProfile(url: Uri){
        launchRequest { userRepository.changeImage(url) }
    }

    fun logout() {
        launchRequest {
            authRepository.logout()
            userRepository.deleteUser()
            _logout.emit(true)
        }
    }

    private fun fetchUser() = launchRequest {
        _currentUser.emit(userRepository.fetchCurrentUser())
    }
}