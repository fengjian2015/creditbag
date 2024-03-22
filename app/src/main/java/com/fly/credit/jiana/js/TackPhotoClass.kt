package com.fly.credit.jiana.js

import android.content.Intent
import android.provider.MediaStore
import android.webkit.WebView
import com.fly.credit.jiana.util.ActivityManager
import com.fly.credit.jiana.util.CommonUtil
import com.fly.credit.jiana.util.Cons
import com.fly.credit.jiana.web.AndroidCallBackJS
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

/**
 * Time：2024/3/16
 * Author：feng
 * Description：
 */
object TackPhotoClass {
    fun tackPhoto(webView: WebView, id: String) {
        try {
            ActivityManager.getCurrentActivity()?.let {
                XXPermissions.with(it)
                    .permission(Permission.CAMERA)
                    .request(object : OnPermissionCallback {
                        override fun onGranted(permissions: MutableList<String>, all: Boolean) {
                            if (all) {
                                Cons.eventTackPhotoId = id
//                                Cons.photoFile = CommonUtil.tackPhoto()
                                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                // 指定使用前置摄像头
                                intent.putExtra("android.intent.extras.CAMERA_FACING", 1)
                                intent.putExtra("android.intent.extras.CAMERA_FACING_FRONT", 1)

                                // 启动相机应用
                                ActivityManager.getCurrentActivity()
                                    ?.startActivityForResult(intent, Cons.TACK_PHOTO)
                            } else {
                                AndroidCallBackJS.callbackJsErrorPermissions(webView, id, Cons.INVOKEFORCREDITBAGTACKPHOTO)
                            }
                        }

                        override fun onDenied(permissions: MutableList<String>, never: Boolean) {
                            AndroidCallBackJS.callbackJsErrorPermissions(webView, id, Cons.INVOKEFORCREDITBAGTACKPHOTO)
                        }
                    })
            }
        } catch (e: Exception) {
            e.printStackTrace()
            AndroidCallBackJS.callbackJsErrorOther(
                webView,
                id,
                Cons.INVOKEFORCREDITBAGTACKPHOTO,
                e.toString()
            )
        }
    }
}