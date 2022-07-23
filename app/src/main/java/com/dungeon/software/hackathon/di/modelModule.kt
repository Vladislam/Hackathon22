package com.dungeon.software.hackathon.di

import com.dungeon.software.hackathon.data.data_source.ChatDataSource
import com.dungeon.software.hackathon.data.data_source.MessageDataSource
import com.dungeon.software.hackathon.data.data_source.StorageDataSource
import com.dungeon.software.hackathon.data.data_source.AuthDataSource
import com.dungeon.software.hackathon.data.data_source.UserDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val modelModule = module {
    single { StorageDataSource() }
    single<ChatDataSource> { ChatDataSource.Base() }
    single<AuthDataSource> { AuthDataSource.Base(FirebaseAuth.getInstance(), androidContext()) }
    single<UserDataSource> { UserDataSource.Base(FirebaseFirestore.getInstance()) }
    single<MessageDataSource> { MessageDataSource.Base() }
}