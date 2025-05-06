package com.app.revechatsdktestapp

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.firebase.FirebaseApp
import com.revesoft.revechatsdk.utils.REVEChatAppLifecycleObserver

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(REVEChatAppLifecycleObserver.INSTANCE)
    }
}