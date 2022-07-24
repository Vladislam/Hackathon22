package com.dungeon.software.hackathon.presentation.splash_screen

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.dungeon.software.hackathon.base.view_model.BaseViewModel
import com.dungeon.software.hackathon.domain.repository.AuthRepository
import com.dungeon.software.hackathon.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class SplashViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _checkedUser = MutableSharedFlow<Boolean>()
    val checkedUser: SharedFlow<Boolean> = _checkedUser

    fun authWithIntent(intent: Intent) {
        launchRequest {
            val user = authRepository.authWithIntent(intent)
            if (!authRepository.checkUser()) {
                userRepository.createUser(user ?: return@launchRequest)
            }
            _checkedUser.emit(true)
        }
    }

    fun isLoggedIn(param: ActivityResultLauncher<Intent>) {
        launchRequest {
            if (authRepository.isLoggedIn()) {
                _checkedUser.emit(true)
            } else {
                authRepository.authWithGoogle(param)
            }
        }
    }
}