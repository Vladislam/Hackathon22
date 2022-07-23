package com.dungeon.software.hackathon.di

import com.dungeon.software.hackathon.presentation.splash_screen.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
}