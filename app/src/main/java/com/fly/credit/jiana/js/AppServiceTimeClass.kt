package com.fly.credit.jiana.js

import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.fly.credit.jiana.MainActivity
import com.fly.credit.jiana.bean.CopyBean
import com.fly.credit.jiana.util.ActivityManager
import com.fly.credit.jiana.util.Cons
import com.fly.credit.jiana.util.DateTool
import com.fly.credit.jiana.web.AndroidCallBackJS
import com.google.gson.Gson

/**
 * Time：2024/3/16
 * Author：feng
 * Description：
 */
object AppServiceTimeClass {
    fun appServiceTime(webView: WebView, id: String, data: Any?) {
        try {
            var commentParseDataBean = Gson().fromJson(data.toString(), CopyBean::class.java)
            DateTool.initTime(commentParseDataBean.value)
            AndroidCallBackJS.callBackJsSuccess(webView,id,Cons.INVOKEFORCREDITBAGAPPSERVICETIME)
        } catch (e: Exception) {
            e.printStackTrace()
            AndroidCallBackJS.callbackJsErrorOther(webView, id, Cons.INVOKEFORCREDITBAGFORWARDOUTSIDE,
                e.toString()
            )
        }
    }
}