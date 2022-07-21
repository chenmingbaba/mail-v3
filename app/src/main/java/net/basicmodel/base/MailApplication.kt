package net.basicmodel.base

import android.app.Application
import com.tencent.mmkv.MMKV

class MailApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
    }
}