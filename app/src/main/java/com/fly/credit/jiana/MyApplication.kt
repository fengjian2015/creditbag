package com.fly.credit.jiana

import android.app.Application
import com.fly.credit.jiana.util.ActivityManager
import com.fly.credit.jiana.util.DateTool
import com.fly.credit.jiana.util.LogUtil
import com.fly.credit.jiana.util.ToastUtil
import com.inter.spareafricaone.component.InstallReferrerManager
import com.tencent.mmkv.MMKV

class MyApplication : Application(){

    companion object {
        @JvmStatic
        lateinit var application: Application
        @JvmStatic
        var open_power = -1 //打开app时的电量，比如 70%
        @JvmStatic
        var complete_apply_power = 0 //提交申请时电量  ，比如70% ，= 0
        @JvmStatic
        var appStartTime: Long = 0
        @JvmStatic
        var dbm: Int = 0
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        MMKV.initialize(this)
        appStartTime = DateTool.getServerTimestamp()
        ActivityManager.registerActivityLifecycleCallbacks()
        LogUtil.setLogEnable(BuildConfig.DEBUG)
        InstallReferrerManager.init(application)
        ToastUtil.getInstance().setContent(application)
    }
}

