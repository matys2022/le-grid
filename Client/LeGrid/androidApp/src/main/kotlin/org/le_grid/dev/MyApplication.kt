package org.le_grid.dev

import android.app.Application
import org.le_grid.dev.di.initKoin
import org.koin.android.ext.koin.androidContext



class MyApplication : Application() {
    override fun onCreate(){
        super.onCreate()
        initKoin {
            androidContext(this@MyApplication)
        }
    }

}