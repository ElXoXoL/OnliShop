package com.example.onlishop.base

import androidx.lifecycle.*
import com.example.onlishop.app.App
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

abstract class BaseViewModel: ViewModel(){

    private var compositeDisposable = CompositeDisposable()

    private val _error = MutableStateFlow<Throwable?>(null)
    val error: LiveData<Throwable> = _error.mapNotNull {
        _error.value = null
        it
    }.asLiveData()

    init {
        App.logger.logExecution("init ${this::class.java.name}")
        if (compositeDisposable.isDisposed) {
            compositeDisposable = CompositeDisposable()
        }
    }

    open fun initOnFragmentCreated() {

    }

    protected fun baseHandler(it: Throwable) {
        App.logger.logDevWithThread("${this::class.java.name} error $it")
        _error.value = it
    }

    fun clearDisposables() {
        compositeDisposable.clear()
    }

    override fun onCleared() {
        App.logger.logExecution("onCleared ${this::class.java.name}")
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.clear()
            compositeDisposable.dispose()
        }
        super.onCleared()
    }

    protected fun CoroutineScope.launchIo(function: suspend () -> Unit): Job {
        return this.launch(Dispatchers.IO) { function() }
    }

    protected fun CoroutineScope.launchMain(function: suspend () -> Unit): Job {
        return this.launch(Dispatchers.Main) { function() }
    }

    protected fun Disposable.toCache() {
        compositeDisposable.add(this)
    }

}