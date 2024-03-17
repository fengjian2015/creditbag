package com.inter.spareafricaone.component

import android.content.Context
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.fly.credit.jiana.util.Cons
import com.fly.credit.jiana.util.LogUtil
import com.fly.credit.jiana.util.MMKVCacheUtil
import org.json.JSONObject

class InstallReferrerManager {

    companion object{
        fun init(context: Context?){
            if (context == null){
                return
            }
            val referrerClient = InstallReferrerClient.newBuilder(context).build()
            referrerClient.startConnection(object :InstallReferrerStateListener{
                override fun onInstallReferrerSetupFinished(p0: Int) {
                    when(p0){
                        InstallReferrerClient.InstallReferrerResponse.OK->{
                            try {
                                referrerClient?.installReferrer?.let {
                                    val jsonObject = JSONObject()
                                    jsonObject.put("install_referrer", it.installReferrer)
                                    jsonObject.put("referrer_click_timestamp_seconds", it.referrerClickTimestampSeconds)
                                    jsonObject.put("referrer_click_timestamp_server_seconds", it.referrerClickTimestampServerSeconds)
                                    jsonObject.put("install_begin_timestamp_seconds", it.installBeginTimestampSeconds)
                                    jsonObject.put("install_begin_timestamp_server_seconds", it.installBeginTimestampServerSeconds)
                                    jsonObject.put("google_play_instant", it.googlePlayInstantParam)
                                    jsonObject.put("install_version", it.installVersion)
                                    val json = jsonObject.toString()
                                    LogUtil.w("installReferrer json : $json")
                                    MMKVCacheUtil.putString(Cons.INSTALL_REFERRER_RESPONSE_JSON,json)
                                }
                                referrerClient.endConnection()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED->{
                            LogUtil.d("FEATURE_NOT_SUPPORTED")

                        }

                        InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE->{
                            LogUtil.d("SERVICE_UNAVAILABLE")

                        }
                    }
                }

                override fun onInstallReferrerServiceDisconnected() {
                    LogUtil.d("onInstallReferrerServiceDisconnected")
                }
            })

        }


    }



}