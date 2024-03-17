package com.fly.credit.jiana.js

import android.content.Context
import android.database.Cursor
import android.provider.CallLog
import android.webkit.WebView
import com.fly.credit.jiana.MyApplication
import com.fly.credit.jiana.bean.ApplyInfoBean
import com.fly.credit.jiana.bean.calllogAuthInfo
import com.fly.credit.jiana.bean.calllogInfo
import com.fly.credit.jiana.network.NewServiceManage
import com.fly.credit.jiana.util.ActivityManager
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
import java.lang.Exception

/**
 * Time：2024/3/16
 * Author：feng
 * Description：
 */
object CalllogInfoClass {
    fun callLog(webView: WebView, id: String) {
        ActivityManager.getCurrentActivity()?.let {
            XXPermissions.with(it)
                .permission(Permission.READ_CALL_LOG)
                .request(object : OnPermissionCallback{
                    override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                        if (allGranted){
                            GlobalScope.launch(Dispatchers.IO){
                                var callLog = getCallLog(MyApplication.application)
                                var calllogAuthInfo = calllogAuthInfo()
                                calllogAuthInfo.create_time = DateTool.getServerTimestamp()
                                calllogAuthInfo.list = callLog
                                withContext(Dispatchers.Main){
                                    LogUtil.d("通讯录获取完成：$calllogAuthInfo")
                                    var applyInfoBean = ApplyInfoBean()
                                    applyInfoBean.calllog_info = calllogAuthInfo
                                    NewServiceManage.uploadApplyInfo(applyInfoBean,webView,id,Cons.INVOKEFORCREDITBAGCALLLOG)
                                }
                            }
                        }else{
                            AndroidCallBackJS.callbackJsErrorPermissions(webView,id, Cons.INVOKEFORCREDITBAGCALLLOG)
                        }
                    }

                    override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                        AndroidCallBackJS.callbackJsErrorPermissions(webView,id, Cons.INVOKEFORCREDITBAGCALLLOG)

                    }
                })
        }
    }

    private fun getCallLog(context: Context):List<calllogInfo>{
        val list = mutableListOf<calllogInfo>()
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER)
            cursor?.apply {
                while (moveToNext()){
                    val name = getString(getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME))
                    val phoneNum = getString(getColumnIndexOrThrow(CallLog.Calls.NUMBER))
                    val callTimestamp = getLong(getColumnIndexOrThrow(CallLog.Calls.DATE))
                    val duration = getInt(getColumnIndexOrThrow(CallLog.Calls.DURATION))
                    val callType = getInt(getColumnIndexOrThrow(CallLog.Calls.TYPE))
                    val id = getString(getColumnIndexOrThrow(CallLog.Calls._ID))
                    val callLogDt = calllogInfo()
                    callLogDt.callName = name
                    callLogDt.callNumber = phoneNum
                    callLogDt.callTime = DateTool.getTimeFromLongYMDHMS(callTimestamp)
                    callLogDt.callDuration = duration.toString()
                    callLogDt.callType = callType
                    callLogDt.id = id
                    list.add(callLogDt)
                }
            }
            cursor?.close()
        }catch (e: Exception){
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return list

    }
}