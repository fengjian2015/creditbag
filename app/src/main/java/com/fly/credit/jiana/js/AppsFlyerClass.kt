package com.fly.credit.jiana.js

import android.webkit.WebView
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.fly.credit.jiana.BuildConfig
import com.fly.credit.jiana.MyApplication
import com.fly.credit.jiana.bean.CopyBean

import com.fly.credit.jiana.manage.UserInfoManage
import com.fly.credit.jiana.util.Cons
import com.fly.credit.jiana.util.LogUtil
import com.fly.credit.jiana.util.LogUtils
import com.fly.credit.jiana.util.MMKVCacheUtil
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
            postAF(it.eventName)
//            FaceBookManage.faceLog(it.eventName)
//            AndroidCallBackJS.callBackJsSuccess(webView,id, Cons.INVOKEFORCREDITBAGAPPSFLYER)
        }
    }

    fun postAF(evenName: String?) {
        var evenName = evenName
        val map = HashMap<String, Any?>()
        val loadId: String
        if (evenName != null && evenName.contains("|")) {
            val split = evenName.split("\\|".toRegex()).toTypedArray()
            loadId = split[1]
            evenName = split[0]
            map["loan_id"] = loadId
        }
        map["mobile"] = UserInfoManage.getUserPhone()
        map["event_code"] = evenName
        AppsFlyerLib.getInstance().logEvent(MyApplication.application, evenName, map)
    }

    fun initAppsFlyer() {
        AppsFlyerLib.getInstance().init("zuWyLLMfwdWYwbpGgkgp8N", object :
            AppsFlyerConversionListener {
            override fun onConversionDataSuccess(map: Map<String, Any>) {
                LogUtils.d("AppsFlyerLib Success: $map")
                for (attrName in map.keys) {
                    if ("af_status" == attrName) {
                        val data = map[attrName] as String?
                        if ("Organic" == data) {
                            MMKVCacheUtil.putString(Cons.KEY_AF_CHANNEL, data)
                        } else if ("Non-organic" == data) {
                            MMKVCacheUtil.putString(Cons.KEY_AF_CHANNEL, map["media_source"].toString())
                        }
                    }
                }
            }

            override fun onConversionDataFail(s: String) {
                LogUtils.d("AppsFlyerLib DataFail: $s")
            }

            override fun onAppOpenAttribution(map: Map<String, String>) {
                LogUtils.d("AppsFlyerLib Attribution: $map")
            }

            override fun onAttributionFailure(s: String) {
                LogUtils.d("AppsFlyerLib AttributionFailure: $s")
            }
        }, MyApplication.application)
        AppsFlyerLib.getInstance().start(MyApplication.application, "zuWyLLMfwdWYwbpGgkgp8N", object :
            AppsFlyerRequestListener {
            override fun onSuccess() {
                LogUtils.d("AppsFlyerLib start Success ")
            }

            override fun onError(i: Int, s: String) {
                LogUtils.d("AppsFlyerLib start Error $s")
            }
        })
        AppsFlyerLib.getInstance().setDebugLog(BuildConfig.DEBUG)
    }
}