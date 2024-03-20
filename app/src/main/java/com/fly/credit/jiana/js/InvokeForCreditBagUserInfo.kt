package com.fly.credit.jiana.js

import android.content.Context
import android.webkit.WebView
import com.fly.credit.jiana.MyApplication
import com.fly.credit.jiana.bean.UserInfoJsCallBackBean
import com.fly.credit.jiana.manage.UserInfoManage
import com.fly.credit.jiana.util.Cons
import com.fly.credit.jiana.web.AndroidCallBackJS
import com.google.gson.Gson

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
object InvokeForCreditBagUserInfo {
    //获取用户信息
    public fun invokeForCreditBagUserInfo(id: String,webView : WebView){
        var userInfoJsCallBackBean = UserInfoJsCallBackBean()
        userInfoJsCallBackBean.devForCreditBagName="android"
        userInfoJsCallBackBean.loginForCreditBagData = UserInfoManage.getUserInfoJson()
        userInfoJsCallBackBean.appForCreditBagVersion = getVersionName(MyApplication.application)
        AndroidCallBackJS.callBackJsSuccess(webView,id
            , Cons.INVOKEFORCREDITBAGUSERINFO
            , Gson().toJson(userInfoJsCallBackBean))
    }

    fun getVersionName(context: Context?): String {
        if (context == null) {
            return ""
        }
        var versionName = ""
        try {
            versionName =
                context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return versionName
    }
}