package com.dungeon.software.hackathon

import android.app.Application
import com.dungeon.software.hackathon.di.modelModule
import com.dungeon.software.hackathon.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(
                viewModelModule,
                modelModule
            ))
        }
    }
}