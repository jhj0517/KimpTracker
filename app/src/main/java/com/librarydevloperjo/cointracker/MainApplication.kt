package com.librarydevloperjo.cointracker

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import androidx.work.Configuration
import javax.inject.Inject


@HiltAndroidApp
class MainApplication : Application(){

    override fun onCreate() {
        super.onCreate()
    }
}