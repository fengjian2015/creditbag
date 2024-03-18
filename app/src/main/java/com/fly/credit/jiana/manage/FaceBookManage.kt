package com.fly.credit.jiana.manage

import com.facebook.appevents.AppEventsLogger
import com.fly.credit.jiana.util.ActivityManager


/**
 * Time：2024/3/18
 * Author：feng
 * Description：
 */
object FaceBookManage {
    fun faceLog(eventString: String){
        val logger = ActivityManager.getCurrentActivity()?.let { AppEventsLogger.newLogger(it) }
        logger?.let {
            it.logEvent(eventString)
        }
    }
}