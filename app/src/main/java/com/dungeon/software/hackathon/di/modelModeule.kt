package com.dungeon.software.hackathon.di

import com.dungeon.software.hackathon.data.repository.StorageDataSource
import org.koin.dsl.module

val modelModule = module {
    single { StorageDataSource() }
}