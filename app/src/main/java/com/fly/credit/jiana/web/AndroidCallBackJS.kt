package com.fly.credit.jiana.web

import android.app.Activity
import android.content.Context
import android.webkit.ValueCallback
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.fly.credit.jiana.MyApplication
import com.fly.credit.jiana.R
import com.fly.credit.jiana.bean.JsCallBackBean
import com.fly.credit.jiana.util.LogUtil
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object AndroidCallBackJS {
    val SUCCESS: String = "0"
    val EROOR_OTHER: String = "999"

    fun callBackJsSuccess(webView: WebView, id: String, event: String) {
        callBackJs(webView, JsCallBackBean(id, event))
    }

    fun callBackJsSuccess(webView: WebView, id: String, event: String,data:String) {
        callBackJs(webView, JsCallBackBean(id, event,data,SUCCESS,null))
    }

    fun callBackJsErrorNet(webView: WebView, id: String, event: String) {
        callBackJs(webView, JsCallBackBean(id, event, null, EROOR_OTHER, null))
    }

    fun callBackJsSuccess(webView: WebView, id: String, event: String, fragment : Fragment) {
        callBackJs(webView, JsCallBackBean(id, event))
    }

    fun callBackJsSuccess(webView: WebView, id: String, event: String,data:String,activity : Activity) {
        callBackJs(webView, JsCallBackBean(id, event,data,SUCCESS,null))
    }

    fun callBackJsErrorNet(webView: WebView, id: String, event: String,context: Context) {
        callBackJs(webView, JsCallBackBean(id, event, null, EROOR_OTHER, null))
    }

    fun callbackJsErrorPermissions(webView: WebView, id: String, event: String) {
        callBackJs(webView, JsCallBackBean(id, event, null, EROOR_OTHER, MyApplication.application.getString(
            R.string.permission_denied)))
    }

    fun callbackJsErrorOther(webView: WebView, id: String, event: String, errorMsg: String) {
        callBackJs(webView, JsCallBackBean(id, event, null, EROOR_OTHER, errorMsg))
    }

    fun callBackJs(webView: WebView, model: JsCallBackBean?) {
        callBackJs(webView, model, null)
    }

    private fun callBackJs(webView: WebView, model: JsCallBackBean?, callback: ValueCallback<*>?) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val toJson = Gson().toJson(model)
                LogUtil.d("js回传：$toJson")
                val js = "javascript: window.JSForCreditBagCallback && window.JSForCreditBagCallback($toJson);"
                webView.evaluateJavascript(js, callback as ValueCallback<String>?)
            } catch (w: Throwable) {
            }
        }
    }
}