package com.dungeon.software.hackathon.di

import com.dungeon.software.hackathon.domain.repository.AuthRepository
import com.dungeon.software.hackathon.domain.repository.ChatRepository
import com.dungeon.software.hackathon.domain.repository.UserRepository
import org.koin.dsl.module

val repositoriesModule = module {
    single<AuthRepository> { AuthRepository.Base(get()) }
    single<UserRepository> { UserRepository.Base(get()) }
    single<ChatRepository> { ChatRepository.Base(get(), get(), get()) }
}