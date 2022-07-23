package com.dungeon.software.hackathon.presentation.splash_screen

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.dungeon.software.hackathon.base.view_model.BaseViewModel
import com.dungeon.software.hackathon.data.repository.AuthDataSource
import com.dungeon.software.hackathon.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class SplashViewModel(
    private val authDataSource: AuthDataSource,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _checkedUser = MutableSharedFlow<Boolean>()
    val checkedUser: SharedFlow<Boolean> = _checkedUser

    fun authWithIntent(intent: Intent) {
        launchRequest {
            val user = authDataSource.authWithIntent(intent)
            userRepository.createUser(user ?: return@launchRequest)
            _checkedUser.emit(true)
        }
    }

    // TODO: fix it
    fun checkUser(intent: Intent) {
        launchRequest {
            if (authDataSource.checkUser()) {
                val user = authDataSource.authWithIntent(intent)
                userRepository.createUser(user ?: return@launchRequest)
                _checkedUser.emit(true)
            } else {
                userRepository.fetchCurrentUser()
            }
        }
    }

    fun isLoggedIn(param: ActivityResultLauncher<Intent>) {
        launchRequest {
            if (authDataSource.isLoggedIn()) {
                _checkedUser.emit(true)
            } else {
                authDataSource.authWithGoogle(param)
            }
        }
    }
}