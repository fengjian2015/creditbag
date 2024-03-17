package com.fly.credit.jiana.js

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.webkit.WebView
import com.fly.credit.jiana.MyApplication
import com.fly.credit.jiana.bean.CopyBean
import com.fly.credit.jiana.util.Cons
import com.fly.credit.jiana.web.AndroidCallBackJS
import com.google.gson.Gson

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
object CopyClipClass {
    fun copyClip(id:String ,webView: WebView,data: Any?){
        try {
            var commentParseDataBean =
                Gson().fromJson(data.toString(), CopyBean::class.java)
            val clipboardManager =
                MyApplication.application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Label", commentParseDataBean.value)
            clipboardManager.setPrimaryClip(clipData)
            AndroidCallBackJS.callBackJsSuccess(webView, id, Cons.INVOKEFORCREDITBAGCOPYCLIP)
        } catch (e: Exception) {
            e.printStackTrace()
            AndroidCallBackJS.callbackJsErrorOther(webView, id, Cons.INVOKEFORCREDITBAGCOPYCLIP, "Los parámetros son incorrectos.")
        }
    }
}