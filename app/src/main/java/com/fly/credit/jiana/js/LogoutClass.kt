package com.fly.credit.jiana.js

import android.content.Intent
import com.fly.credit.jiana.StartActivity
import com.fly.credit.jiana.manage.UserInfoManage
import com.fly.credit.jiana.util.ActivityManager

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
object LogoutClass {
    fun logout(){
        UserInfoManage.logout()
        val intent = Intent(ActivityManager.getCurrentActivity(), StartActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        ActivityManager.getCurrentActivity()?.startActivity(intent)
    }
}