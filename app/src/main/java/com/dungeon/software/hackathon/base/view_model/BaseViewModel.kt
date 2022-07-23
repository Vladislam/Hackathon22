package com.dungeon.software.hackathon.base.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dangeon.software.notes.util.pop_up.CustomError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel() {

    protected val _loadingFlow = MutableStateFlow(false)
    val loadingFlow : StateFlow<Boolean> = _loadingFlow

    protected val _errorMessage = MutableSharedFlow<String>()
    val errorMessage : SharedFlow<String> = _errorMessage

    protected fun launchRequest(
        context: CoroutineContext = Dispatchers.IO,
        scope: CoroutineScope = viewModelScope,
        block : suspend CoroutineScope.() -> Unit
    ) = scope.launch {
        try {
            _loadingFlow.emit(true)
            withContext(context){
                block.invoke(this)
            }
        } catch (e: Exception){
            CustomError.parse(e)
            _errorMessage.emit(e.localizedMessage)
        } finally {
            _loadingFlow.emit(false)
        }
    }
}