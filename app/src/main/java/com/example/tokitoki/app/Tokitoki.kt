package com.example.tokitoki.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Tokitoki : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}