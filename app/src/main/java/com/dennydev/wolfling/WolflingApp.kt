package com.dennydev.wolfling

import android.app.Application
import com.dennydev.wolfling.model.common.Constant
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WolflingApp: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}