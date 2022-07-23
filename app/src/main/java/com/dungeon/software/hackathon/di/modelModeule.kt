package com.dungeon.software.hackathon.di

import com.dungeon.software.hackathon.data.data_source.ChatDataSource
import com.dungeon.software.hackathon.data.data_source.StorageDataSource
import org.koin.dsl.module

val modelModule = module {
    single { StorageDataSource() }
    single { ChatDataSource.Base() }
}