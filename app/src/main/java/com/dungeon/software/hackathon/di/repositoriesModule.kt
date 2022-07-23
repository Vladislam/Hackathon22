package com.dungeon.software.hackathon.di

import com.dungeon.software.hackathon.data.repository.AuthDataSource
import com.dungeon.software.hackathon.data.repository.ChatRepository
import com.dungeon.software.hackathon.data.repository.UserDataSource
import com.dungeon.software.hackathon.domain.repository.AuthRepository
import com.dungeon.software.hackathon.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoriesModule = module {
    single<AuthDataSource> { AuthDataSource.Base(FirebaseAuth.getInstance(), androidContext()) }
    single<UserDataSource> { UserDataSource.Base(FirebaseFirestore.getInstance()) }
    single<AuthRepository> { AuthRepository.Base(get()) }
    single<UserRepository> { UserRepository.Base(get()) }
    single<ChatRepository> { ChatRepository.Base(get(), get(), get()) }
}