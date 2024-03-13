package com.fly.credit.jiana

import android.app.Application
import com.fly.credit.jiana.network.HttpClient
import com.fly.credit.jiana.util.ActivityManager
import com.fly.credit.jiana.util.Cons
import com.fly.credit.jiana.util.LogUtil
import com.tencent.mmkv.MMKV

class MyApplication : Application(){

    companion object {
        @JvmStatic
        lateinit var application: Application
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        ActivityManager.registerActivityLifecycleCallbacks()
        LogUtil.setDebug(BuildConfig.DEBUG)
        HttpClient.instance.init(Cons.BaseUrl)
        MMKV.initialize(this)
    }
}

