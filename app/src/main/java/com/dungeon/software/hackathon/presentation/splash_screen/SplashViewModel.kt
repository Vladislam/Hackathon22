package com.dungeon.software.hackathon.presentation.splash_screen

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.viewModelScope
import com.dungeon.software.hackathon.base.view_model.BaseViewModel
import com.dungeon.software.hackathon.data.repository.AuthDataSource
import com.dungeon.software.hackathon.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val authDataSource: AuthDataSource,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _checkedUser = MutableSharedFlow<Boolean>()
    val checkedUser: SharedFlow<Boolean> = _checkedUser

    fun authWithIntent(intent: Intent) {
        viewModelScope.launch {
            val user = authDataSource.authWithIntent(intent)
            userRepository.createUser(user!!)
            _checkedUser.emit(user != null)
        }
    }

    fun isLoggedIn(param: ActivityResultLauncher<Intent>) {
        viewModelScope.launch {
            if (authDataSource.isLoggedIn()) {
                _checkedUser.emit(true)
            } else {
                authDataSource.authWithGoogle(param)
            }
        }
    }
}