package com.dungeon.software.hackathon.di

import com.dangeon.software.notes.util.pop_up.PopUpManager
import com.dungeon.software.hackathon.util.SharedPreferencesManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val managerModule = module {
    single { PopUpManager(androidContext()) }
    single { SharedPreferencesManager(androidContext()) }
}