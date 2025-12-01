package com.autoservice.mobile

import android.app.Application
import com.autoservice.mobile.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AutoserviceApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AutoserviceApp)
            modules(appModule)
        }
    }
}
