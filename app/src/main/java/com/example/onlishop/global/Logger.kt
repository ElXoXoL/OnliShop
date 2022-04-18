package com.example.onlishop.global

import android.util.Log
import kotlin.reflect.KClass

enum class LogType {
    Default,
    FuncCall,
    ApiCall,
    Error
}

class Logger {

    private val isLogsEnabled = true

    private val String.appSigned: String
        get() = "PlanB_$this"

    private fun log(obj: Any?, logTag: String){
        if (isLogsEnabled) Log.d(logTag, obj.toString())
    }

    fun logExecution(obj: Any?){
        log(obj, LogType.FuncCall.name.appSigned)
    }

    fun logApi(obj: Any?){
        log(obj, LogType.ApiCall.name.appSigned)
    }

    fun logDev(obj: Any?){
        log(obj, LogType.Default.name.appSigned)
    }

    fun logDevWithThread(obj: Any?){
        log("$obj thread ${Thread.currentThread().name}", LogType.Default.name.appSigned)
    }

    fun logTag(obj: Any?, logTag: String){
        log(obj, logTag.appSigned)
    }

    fun <T: Any> exception(obj: Any?, clazz: KClass<T>){
        if (isLogsEnabled) Log.e(clazz::class.java.name.appSigned, obj.toString())
    }

    fun <T: Any> logTagClass(obj: Any?, clazz: KClass<T>){
        if (isLogsEnabled) Log.d(clazz::class.java.name.appSigned, obj.toString())
    }
}