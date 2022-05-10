package com.example.onlishop.global

import android.util.Log
import kotlin.reflect.KClass

enum class LogType {
    Default,
    FuncCall,
    ApiCall,
    Error
}

class LoggerImpl: Logger {

    private val isLogsEnabled = true

    private val String.appSigned: String
        get() = "PlanB_$this"

    private fun log(obj: Any?, logTag: String){
        if (isLogsEnabled) Log.d(logTag, obj.toString())
    }

    override fun logExecution(obj: Any?){
        log(obj, LogType.FuncCall.name.appSigned)
    }

    override fun logApi(obj: Any?){
        log(obj, LogType.ApiCall.name.appSigned)
    }

    override fun logDev(obj: Any?){
        log(obj, LogType.Default.name.appSigned)
    }

    override fun logDevWithThread(obj: Any?){
        log("$obj thread ${Thread.currentThread().name}", LogType.Default.name.appSigned)
    }

    override fun logTag(obj: Any?, logTag: String){
        log(obj, logTag.appSigned)
    }

    override fun <T: Any> exception(obj: Any?, clazz: KClass<T>){
        if (isLogsEnabled) Log.e(clazz::class.java.name.appSigned, obj.toString())
    }

    override fun <T: Any> logTagClass(obj: Any?, clazz: KClass<T>){
        if (isLogsEnabled) Log.d(clazz::class.java.name.appSigned, obj.toString())
    }
}

interface Logger {

    fun logExecution(obj: Any?)

    fun logApi(obj: Any?)

    fun logDev(obj: Any?)

    fun logDevWithThread(obj: Any?)

    fun logTag(obj: Any?, logTag: String)

    fun <T: Any> exception(obj: Any?, clazz: KClass<T>)

    fun <T: Any> logTagClass(obj: Any?, clazz: KClass<T>)

}