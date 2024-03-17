package com.fly.credit.jiana.js

import android.webkit.WebView
import com.fly.credit.jiana.bean.CopyBean
import com.fly.credit.jiana.bean.ImageResponse
import com.fly.credit.jiana.network.NetUpload
import com.fly.credit.jiana.util.Cons
import com.fly.credit.jiana.util.LoadingUtil
import com.fly.credit.jiana.web.AndroidCallBackJS
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.File
import java.io.IOException

/**
 * Time：2024/3/17
 * Author：feng
 * Description：
 */
object UpImageClass {
    fun upImage(webView: WebView, id: String, file: File) {
        NetUpload.okHttpUploadImage(file, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LoadingUtil.dismiss()
                AndroidCallBackJS.callbackJsErrorOther(webView,id, Cons.INVOKEFORCREDITBAGTACKPHOTO, e.toString())
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                LoadingUtil.dismiss()
                if (response.isSuccessful) {
                    try {
                        val responseBody: String = NetUpload.getResponseBody(response)
                        val imageResponse: ImageResponse =
                            Gson().fromJson(responseBody, ImageResponse::class.java)
                        val copyBean = CopyBean()
                        copyBean.value = imageResponse.data.toString()
                        AndroidCallBackJS.callBackJsSuccess(webView,id,Cons.INVOKEFORCREDITBAGTACKPHOTO
                            , Gson().toJson(copyBean))

                    } catch (e: Exception) {
                        e.printStackTrace()
                        AndroidCallBackJS.callbackJsErrorOther(webView,id, Cons.INVOKEFORCREDITBAGTACKPHOTO, e.toString())
                    }
                } else {
                    AndroidCallBackJS.callbackJsErrorPermissions(webView,id, Cons.INVOKEFORCREDITBAGTACKPHOTO)
                }
            }
        })
    }
}