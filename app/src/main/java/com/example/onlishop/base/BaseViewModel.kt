package com.example.onlishop.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

abstract class BaseViewModel: ViewModel(){

    override fun onCleared() {
        super.onCleared()
    }

    protected fun CoroutineScope.launchIo(function: suspend () -> Unit): Job {
        return this.launch(Dispatchers.IO) { function() }
    }

    protected fun CoroutineScope.launchMain(function: suspend () -> Unit): Job {
        return this.launch(Dispatchers.Main) { function() }
    }

}