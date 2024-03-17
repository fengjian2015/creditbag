package com.fly.credit.jiana.js

import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import com.fly.credit.jiana.bean.CopyBean
import com.fly.credit.jiana.util.ActivityManager
import com.fly.credit.jiana.util.Cons
import com.fly.credit.jiana.web.AndroidCallBackJS
import com.google.gson.Gson


/**
 * Time：2024/3/16
 * Author：feng
 * Description：
 */
object OpenBrowserClass {
    fun openBrowser(webView: WebView, id: String, data: Any?) {
        var commentParseDataBean = Gson().fromJson(data.toString(), CopyBean::class.java)
        try {
            var url = commentParseDataBean.value
            if (url != null && !url.startsWith("http") && !url.startsWith("file")) {
                url = "https://$url"
            }
            val uri: Uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            ActivityManager.getCurrentActivity()?.startActivity(intent)
            AndroidCallBackJS.callBackJsSuccess(webView, id, Cons.INVOKEFORCREDITBAGOPENBROWSER)
        }catch (e: Exception){
            e.printStackTrace()
            AndroidCallBackJS.callbackJsErrorOther(webView, id, Cons.INVOKEFORCREDITBAGOPENBROWSER,
                e.toString()
            )
        }
    }
}