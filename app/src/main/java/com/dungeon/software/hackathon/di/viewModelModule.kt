package com.dungeon.software.hackathon.di

import com.dungeon.software.hackathon.presentation.chat_screen.ChatViewModel
import com.dungeon.software.hackathon.presentation.chats_list_screen.ChatsListViewModel
import com.dungeon.software.hackathon.presentation.friend_search_screen.FriendSearchViewModel
import com.dungeon.software.hackathon.presentation.group_chat_details_screen.GroupChatDetailsViewModel
import com.dungeon.software.hackathon.presentation.group_chat_screen.GroupChatViewModel
import com.dungeon.software.hackathon.presentation.opponent_user_details_screen.OpponentUserDetailsViewModel
import com.dungeon.software.hackathon.presentation.splash_screen.SplashViewModel
import com.dungeon.software.hackathon.presentation.user_details_screen.UserDetailsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val viewModelModule = module {
    viewModel { SplashViewModel(get(), get()) }
    viewModel { UserDetailsViewModel(get(), get()) }
    viewModel { FriendSearchViewModel(get(), get()) }
    viewModel { ChatsListViewModel(get()) }
    viewModel { ChatViewModel(get(), get(), get()) }
    viewModel { OpponentUserDetailsViewModel(get()) }
    viewModel { GroupChatDetailsViewModel(get()) }
    viewModel { GroupChatViewModel(get(), get()) }
}