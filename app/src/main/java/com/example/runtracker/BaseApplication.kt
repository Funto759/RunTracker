package com.example.runtracker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import pub.devrel.easypermissions.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class BaseApplication : Application()  {
    override fun onCreate() {
        super.onCreate()
            Timber.plant(Timber.DebugTree()) // Plant DebugTree for debug builds

    }
}