package com.fly.credit.jiana.js

import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.fly.credit.jiana.MainActivity
import com.fly.credit.jiana.bean.CopyBean
import com.fly.credit.jiana.util.ActivityManager
import com.fly.credit.jiana.util.Cons
import com.fly.credit.jiana.util.SparedUtils
import com.fly.credit.jiana.util.ToastUtil
import com.fly.credit.jiana.web.AndroidCallBackJS
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Time：2024/3/16
 * Author：feng
 * Description：
 */
object OpenNewWebClass {
    fun openNew(webView: WebView, id: String, data: Any?) {
        try {
            val s = Gson().toJson(data)
            var commentParseDataBean = Gson().fromJson(s, CopyBean::class.java)
            MainActivity.openWeb(
                ActivityManager.getCurrentActivity() as AppCompatActivity,
                false,
                commentParseDataBean.value)
            AndroidCallBackJS.callBackJsSuccess(webView,id,Cons.INVOKEFORCREDITBAGFORWARDOUTSIDE)
        } catch (e: Exception) {
            e.printStackTrace()
            AndroidCallBackJS.callbackJsErrorOther(webView, id, Cons.INVOKEFORCREDITBAGFORWARDOUTSIDE,
                e.printStackTrace().toString()
            )
        }
    }
}