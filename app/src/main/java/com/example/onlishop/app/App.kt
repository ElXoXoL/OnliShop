package com.example.onlishop.app

import android.app.Application
import com.example.onlishop.database.RoomDatabase
import com.example.onlishop.global.Logger
import com.example.onlishop.koin.app
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App: Application(){

    companion object {
        lateinit var instance: App
        lateinit var logger: Logger
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            // declare used Android context
            androidLogger(Level.NONE)
            androidContext(this@App)
            // declare modules
            modules(
                app
            )
        }
        logger = get()
    }

//    override fun attachBaseContext(base: Context) {
//        super.attachBaseContext(LocaleUtils.setLocale(base))
//        instance = this
//    }
//
//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        LocaleUtils.setLocale(this)
//        instance = this
//    }
}