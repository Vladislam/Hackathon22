package com.dungeon.software.hackathon.presentation.splash_screen

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.viewModelScope
import com.dungeon.software.hackathon.base.view_model.BaseViewModel
import com.dungeon.software.hackathon.data.repository.AuthRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
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
            val user = repository.authWithIntent(intent)
            _checkedUser.emit(user != null)
        }
    }

    fun isLoggedIn(param: ActivityResultLauncher<Intent>) {
        viewModelScope.launch {
            if (repository.isLoggedIn()) {
                _checkedUser.emit(true)
            } else {
                repository.authWithGoogle(param)
            }
        }
    }
}