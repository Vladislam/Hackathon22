package com.dungeon.software.hackathon.util.ext

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

fun <T> Flow<T>.throttleFirst(periodMillis: Long): Flow<T> {
    require(periodMillis > 0) { "period should be positive" }
    return flow {
        var lastTime = 0L
        collect { value ->
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastTime >= periodMillis) {
                lastTime = currentTime
                emit(value)
            }
        }
    }
}

fun View.throttleClicks(onEach: () -> Unit, scope: CoroutineScope, periodMillis: Long = 100L) {
    this.clicks()
        .throttleFirst(periodMillis)
        .onEach {
            onEach.invoke()
        }
        .launchIn(scope)
}