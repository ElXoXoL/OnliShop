package com.example.onlishop.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlishop.app.App
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapNotNull

abstract class BaseViewModel: ViewModel() {

    val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        _error.value = throwable
    }

    private val _error = MutableStateFlow<Throwable?>(null)
    val error: LiveData<Throwable> = _error.mapNotNull {
        _error.value = null
        it
    }.asLiveData()

    protected val mainDispatcherHandled = Dispatchers.Main + errorHandler

    init {
        App.logger.logExecution("init ${this::class.java.name}")
    }

    override fun onCleared() {
        App.logger.logExecution("onCleared ${this::class.java.name}")
        super.onCleared()
    }

    protected fun CoroutineScope.launchIo(function: suspend CoroutineScope.() -> Unit): Job {
        return this.launch(Dispatchers.IO + errorHandler, block = function)
    }

    protected fun CoroutineScope.launchMain(function: suspend CoroutineScope.() -> Unit): Job {
        return this.launch(Dispatchers.Main + errorHandler, block = function)
    }

}