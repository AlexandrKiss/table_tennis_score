package com.kiss.tabletennisscore

import android.app.Application
import com.kiss.tabletennisscore.common.Preferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TennisApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Preferences.init(this)
    }
}