package com.dungeon.software.hackathon.base.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dangeon.software.notes.util.pop_up.CustomError
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel() {

    protected val _loadingFlow = MutableStateFlow(false)
    val loadingFlow: SharedFlow<Boolean> = _loadingFlow

    protected val _error = Channel<CustomError>()
    val error: Flow<CustomError> = _error.consumeAsFlow()

    protected fun launchRequest(
        context: CoroutineContext = Dispatchers.IO,
        scope: CoroutineScope = viewModelScope,
        block: suspend CoroutineScope.() -> Unit
    ) = scope.launch {
        try {
            _loadingFlow.emit(true)
            withContext(context) {
                block.invoke(this)
            }
        } catch (e: Exception) {
            CustomError.parse(e)
        } finally {
            _loadingFlow.emit(false)
        }
    }
}