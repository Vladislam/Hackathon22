package com.dungeon.software.hackathon.data.repository

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.dangeon.software.notes.util.pop_up.CustomError
import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.extensions.toUserDto
import com.dungeon.software.hackathon.data.models.UserDto
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

interface AuthRepository {

    suspend fun isLoggedIn() : Boolean

    suspend fun authWithIntent(intent: Intent) : UserDto?

    suspend fun authWithGoogle(param: ActivityResultLauncher<Intent>)

    class Base(
        private val firebaseAuth: FirebaseAuth,
        private val context: Context
        ) : AuthRepository {

        override suspend fun isLoggedIn(): Boolean {
            return firebaseAuth.currentUser != null && firebaseAuth.currentUser?.isAnonymous == false
        }

        override suspend fun authWithIntent(intent: Intent): UserDto? {
            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            if (task.isSuccessful) {
                try {
                    val account = task.getResult(ApiException::class.java)
                    return firebaseAuthWithGoogle(account)

                } catch (e: Exception) {
                    e.printStackTrace()
                    throw CustomError.SomethingWentWrong
                }
            }
            return null
        }

        private suspend fun firebaseAuthWithGoogle(account: GoogleSignInAccount?): UserDto? {
            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
            if(firebaseAuth.currentUser?.isAnonymous == true) {
                firebaseAuth.currentUser?.delete()
            }
            firebaseAuth.signInWithCredential(credential).await()
            return account.toUserDto()
        }

        override suspend fun authWithGoogle(param: ActivityResultLauncher<Intent>) {

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(context, gso)
            param.launch(googleSignInClient.signInIntent)
        }
    }
}