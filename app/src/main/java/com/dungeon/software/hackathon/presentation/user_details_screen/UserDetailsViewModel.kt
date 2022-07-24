package com.dungeon.software.hackathon.presentation.user_details_screen

import android.content.Context
import android.net.Uri
import com.dungeon.software.hackathon.base.view_model.BaseViewModel
import com.dungeon.software.hackathon.domain.models.User
import com.dungeon.software.hackathon.domain.repository.AuthRepository
import com.dungeon.software.hackathon.domain.repository.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent
import org.koin.core.component.get


class UserDetailsViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : BaseViewModel(), KoinComponent {

    private val _currentUser = MutableSharedFlow<User>()
    val currentUser: SharedFlow<User> = _currentUser

    private val _logout = MutableSharedFlow<Boolean>()
    val logout: SharedFlow<Boolean> = _logout

    init {
        fetchUser()
    }

    fun updateImageProfile(url: Uri) {
        launchRequest { userRepository.changeImage(url) }
    }

    fun deleteFile(url: String) {
        launchRequest { userRepository.deleteFile(url) }
    }

    fun changeName(name: String) {
        launchRequest { userRepository.changeName(name) }
    }

    fun logout() {
        launchRequest {
            GoogleSignIn.getClient(
                get<Context>(), GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("12243085829-b4grfg7t41m9k2u8nqg4f4sgoc7m60o2.apps.googleusercontent.com")
                    .requestEmail()
                    .build()
            ).signOut().await()
            FirebaseAuth.getInstance().signOut()
            _logout.emit(true)
        }
    }

    private fun fetchUser() = launchRequest {
        _currentUser.emit(userRepository.fetchCurrentUser())
    }
}