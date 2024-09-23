package com.example.rollingrockgameadvanced

import android.app.Application
import com.example.rollingrockgameadvanced.Utilities.SharedPreferencesManager

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.init(this)
    }

}