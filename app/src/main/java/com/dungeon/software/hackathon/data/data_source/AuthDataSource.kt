package com.dungeon.software.hackathon.data.data_source

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.data.models.UserDto
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface AuthDataSource {

    suspend fun isLoggedIn(): Boolean

    suspend fun authWithIntent(intent: Intent): UserDto?

    suspend fun authWithGoogle(param: ActivityResultLauncher<Intent>)

    suspend fun checkUser() : Boolean

    suspend fun logout()

    class Base(
        private val firebaseAuth: FirebaseAuth,
        private val context: Context
    ) : AuthDataSource {

        private val firestore = FirebaseFirestore.getInstance()

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

        override suspend fun checkUser(): Boolean = suspendCoroutine { continuation ->
            firestore.collection(UserDataSource.USERS_COLLECTION)
                .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                .get()
                .addOnSuccessListener {
                    if (it.toObject(UserDto::class.java) != null) {
                        continuation.resume(true)
                    } else {
                        continuation.resume(false)
                    }
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }

        private suspend fun createUser(user: UserDto) = suspendCoroutine { continuation ->
            val currentUser = FirebaseAuth.getInstance().currentUser?.uid ?: kotlin.run {
                continuation.resumeWithException(NullPointerException())
                return@suspendCoroutine
            }
            firestore.collection(UserDataSource.USERS_COLLECTION)
                .document(currentUser)
                .set(user)
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
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

        override suspend fun logout() = firebaseAuth.signOut()

    }
}