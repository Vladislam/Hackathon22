package com.dungeon.software.hackathon.di

import com.dungeon.software.hackathon.presentation.chats_list_screen.ChatsListViewModel
import com.dungeon.software.hackathon.presentation.friend_search_screen.FriendSearchViewModel
import com.dungeon.software.hackathon.presentation.opponent_user_details_screen.OpponentUserDetailsViewModel
import com.dungeon.software.hackathon.presentation.splash_screen.SplashViewModel
import com.dungeon.software.hackathon.presentation.user_details_screen.UserDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel(get(), get()) }
    viewModel { UserDetailsViewModel(get(), get()) }
    viewModel { FriendSearchViewModel(get(), get()) }
    viewModel { ChatsListViewModel(get()) }
    viewModel { OpponentUserDetailsViewModel(get()) }
}