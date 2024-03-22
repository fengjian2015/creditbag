package com.fly.credit.jiana.web

import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.annotation.Keep
import com.fly.credit.jiana.bean.JsDataBean
import com.fly.credit.jiana.js.*
import com.fly.credit.jiana.util.ActivityManager
import com.fly.credit.jiana.util.Cons
import com.fly.credit.jiana.util.LogUtil
import com.google.gson.Gson
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
@Keep
class WebJs constructor(context: Context,webView: WebView){
    var context: Context
    var webView : WebView
    private var eventSelectContactId: String = ""



    init {
        this.context = context
        this.webView =webView
    }

    @JavascriptInterface
    fun appForCreditBagInvoke(string: String){
        LogUtil.d("----- js string:$string")
        val model: JsDataBean = Gson().fromJson(string, JsDataBean::class.java)
        jsData(model.data, model.action, model.id)
    }

    private fun jsData(data: Any?, event: String, id: String){
        when(event){
            //获取用户信息
            Cons.INVOKEFORCREDITBAGUSERINFO-> InvokeForCreditBagUserInfo.invokeForCreditBagUserInfo(id,webView)
            Cons.INVOKEFORCREDITBAGCOPYCLIP-> CopyClipClass.copyClip(id,webView,data)//复制到剪切板
            Cons.INVOKEFORCREDITBAGSIGNOUT-> LogoutClass.logout()//退出登录
            Cons.INVOKEFORCREDITBAGDEVICEINFO-> DeviceInfoClass.deviceInfo(context,id,webView,data)//设备信息
            Cons.INVOKEFORCREDITBAGINSTALLATIONINFO-> AppListClass.applist(id,webView)//安装信息
            Cons.INVOKEFORCREDITBAGSMSINFO-> SmsInfoClass.smsInfo(webView,id)//短信信息
            Cons.INVOKEFORCREDITBAGLOCATIONINFO-> LocationInfoClass.locationInfo(webView,id)//定位信息
            Cons.INVOKEFORCREDITBAGCALLLOG-> CalllogInfoClass.callLog(webView,id)//通话记录信息
            Cons.INVOKEFORCREDITBAGCALENDARINFO-> CalendarsInfoClass.calendarsInfo(webView,id)//日历信息
            Cons.INVOKEFORCREDITBAGSELECTCONTACT-> selectContact(webView,id)//选择通讯录人员
            Cons.INVOKEFORCREDITBAGAPPSFLYER-> AppsFlyerClass.event(webView,id,data)//埋点数据
            Cons.INVOKEFORCREDITBAGTACKPHOTO-> TackPhotoClass.tackPhoto(webView,id)//用户头像上传
            Cons.INVOKEFORCREDITBAGFORWARDOUTSIDE-> OpenNewWebClass.openNew(webView,id,data)//使用新的页面打开H5
            Cons.INVOKEFORCREDITBAGAPPSERVICETIME-> AppServiceTimeClass.appServiceTime(webView,id,data)//当前系统服务器时间
            Cons.INVOKEFORCREDITBAGOPENBROWSER-> OpenBrowserClass.openBrowser(webView,id,data)//使用手机默认浏览器打开H5
        }
    }

    fun selectContact(webView: WebView, id: String) {
        ActivityManager.getCurrentActivity()?.let {
            XXPermissions.with(it)
                .permission(Permission.READ_CONTACTS)
                .permission(Permission.GET_ACCOUNTS)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                        if (allGranted){
                            eventSelectContactId = id
                            ActivityManager.getCurrentActivity()?.startActivityForResult(
                                Intent(Intent.ACTION_PICK,  ContactsContract.CommonDataKinds.Phone.CONTENT_URI),
                                Cons.SELECT_CONTACTS_CONTRACT
                            )
                        }else{
                            AndroidCallBackJS.callbackJsErrorPermissions(webView, id, Cons.INVOKEFORCREDITBAGSELECTCONTACT)
                        }
                    }

                    override fun onDenied(
                        permissions: MutableList<String>,
                        doNotAskAgain: Boolean
                    ) {
                        AndroidCallBackJS.callbackJsErrorPermissions(webView, id, Cons.INVOKEFORCREDITBAGSELECTCONTACT)
                    }
                })
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        ActivityResult.onActivityResult(requestCode,resultCode,data,webView,eventSelectContactId)
    }

}