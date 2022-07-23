package com.dungeon.software.hackathon.di

import com.dungeon.software.hackathon.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoriesModule = module {
    single<AuthRepository> { AuthRepository.Base(FirebaseAuth.getInstance(), androidContext()) }
}