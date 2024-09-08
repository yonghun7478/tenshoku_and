package com.example.tenshoku_and.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Tenshoku : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}