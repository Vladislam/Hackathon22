package com.dungeon.software.hackathon

import android.app.Application
import com.dungeon.software.hackathon.di.modelModule
import com.dungeon.software.hackathon.di.repositoriesModule
import com.dungeon.software.hackathon.di.viewModelModule
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        FirebaseApp.initializeApp(this@App)

        startKoin {
            androidContext(this@App)
            modules(listOf(
                viewModelModule,
                modelModule,
                repositoriesModule
            ))
        }
    }
}