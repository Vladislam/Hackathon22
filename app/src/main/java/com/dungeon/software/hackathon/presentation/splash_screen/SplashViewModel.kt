package com.dungeon.software.hackathon.presentation.splash_screen

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.viewModelScope
import com.dungeon.software.hackathon.base.view_model.BaseViewModel
import com.dungeon.software.hackathon.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val repository: AuthRepository
) : BaseViewModel() {

    private val _checkedUser = MutableSharedFlow<Boolean>()
    val checkedUser: SharedFlow<Boolean> = _checkedUser

    fun authWithIntent(intent: Intent) {
        viewModelScope.launch {
            repository.authWithIntent(intent)
        }
    }

    fun isLoggedIn(param: ActivityResultLauncher<Intent>) {
        viewModelScope.launch {
            if (repository.isLoggedIn()) {
                repository.authWithGoogle(param)
                _checkedUser.emit(true)
            } else {
                _checkedUser.emit(false)
            }
        }
    }
}