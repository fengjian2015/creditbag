package com.fly.credit.jiana.js

import android.webkit.WebView
import com.fly.credit.jiana.bean.CopyBean
import com.fly.credit.jiana.manage.FaceBookManage
import com.fly.credit.jiana.util.Cons
import com.fly.credit.jiana.util.LogUtil
import com.fly.credit.jiana.web.AndroidCallBackJS
import com.google.gson.Gson

/**
 * Time：2024/3/18
 * Author：feng
 * Description：
 */
object AppsFlyerClass {
    fun event(webView: WebView, id: String, data: Any?) {
        var commentParseDataBean = Gson().fromJson(data.toString(), CopyBean::class.java)
        commentParseDataBean?.let {
            LogUtil.d("埋点：${it.eventName}")
            FaceBookManage.faceLog(it.eventName)
            AndroidCallBackJS.callBackJsSuccess(webView,id, Cons.INVOKEFORCREDITBAGAPPSFLYER)
        }
    }
}