package com.fly.credit.jiana.js

import android.webkit.WebView
import com.fly.credit.jiana.bean.ApplyInfoBean
import com.fly.credit.jiana.bean.SmsAuthInfo
import com.fly.credit.jiana.network.NewServiceManage
import com.fly.credit.jiana.util.ActivityManager
import com.fly.credit.jiana.util.AuthDataUtil
import com.fly.credit.jiana.util.Cons
import com.fly.credit.jiana.util.DateTool
import com.fly.credit.jiana.util.LogUtil
import com.fly.credit.jiana.web.AndroidCallBackJS
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Time：2024/3/16
 * Author：feng
 * Description：
 */
object SmsInfoClass {
    fun smsInfo(webView: WebView, id: String) {
        ActivityManager.getCurrentActivity()?.let {
            XXPermissions.with(it)
                .permission(Permission.READ_PHONE_STATE)
                .permission(Permission.READ_SMS)
                .permission(Permission.READ_CONTACTS)
                .permission(Permission.GET_ACCOUNTS)
                .request(object : OnPermissionCallback{
                    override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                        if (allGranted){
                            GlobalScope.launch(Dispatchers.IO){
                                var smsInfo=AuthDataUtil.getSmsInfo()
                                var smsAuthInfo = SmsAuthInfo()
                                smsAuthInfo.create_time = DateTool.getServerTimestamp()
                                smsAuthInfo.list = smsInfo
                                withContext(Dispatchers.Main){
                                    LogUtil.d("通讯录加载完成：$smsInfo")

                                    var applyInfoBean = ApplyInfoBean()
                                    applyInfoBean.sms = smsAuthInfo
                                    NewServiceManage.uploadApplyInfo(applyInfoBean,webView,id,Cons.INVOKEFORCREDITBAGSMSINFO)
                                }
                            }
                        }else{
                            AndroidCallBackJS.callbackJsErrorPermissions(webView,id, Cons.INVOKEFORCREDITBAGSMSINFO)
                        }
                    }

                    override fun onDenied(
                        permissions: MutableList<String>,
                        doNotAskAgain: Boolean
                    ) {
                        AndroidCallBackJS.callbackJsErrorPermissions(webView,id, Cons.INVOKEFORCREDITBAGSMSINFO)
                    }
                })
        }
    }


}
