package com.mivanovskaya.gitviewer.androidapp

import android.app.Application
import com.mivanovskaya.gitviewer.androidapp.di.androidModule
import com.mivanovskaya.gitviewer.shared.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(appModule() + androidModule)
        }
    }
}
