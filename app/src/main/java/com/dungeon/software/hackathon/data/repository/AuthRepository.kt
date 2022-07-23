package com.dungeon.software.hackathon.data.repository

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.data.models.UserDto
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

interface AuthRepository {

    suspend fun isLoggedIn(): Boolean

    suspend fun authWithIntent(intent: Intent): UserDto?

    suspend fun authWithGoogle(param: ActivityResultLauncher<Intent>)

    class Base(
        private val firebaseAuth: FirebaseAuth,
        private val context: Context
    ) : AuthRepository {

        override suspend fun isLoggedIn(): Boolean {
            return firebaseAuth.currentUser != null && firebaseAuth.currentUser?.isAnonymous == false
        }

        override suspend fun authWithIntent(intent: Intent) = firebaseAuthWithGoogle(GoogleSignIn.getSignedInAccountFromIntent(intent).await())

        private suspend fun firebaseAuthWithGoogle(account: GoogleSignInAccount?): UserDto? {
            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
            firebaseAuth.signInWithCredential(credential).await()
            return account.toUserDto()
        }

        override suspend fun authWithGoogle(param: ActivityResultLauncher<Intent>) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.API_KEY))
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(context, gso)
            param.launch(googleSignInClient.signInIntent)
        }

        private fun GoogleSignInAccount?.toUserDto() = this?.run {
            UserDto(
                uid = this.id ?: "-",
                name = this.displayName ?: "-",
                email = this.email ?: "-",
                imageUrl = this.photoUrl?.toString(),
                friends = listOf()
            )
        }
    }
}