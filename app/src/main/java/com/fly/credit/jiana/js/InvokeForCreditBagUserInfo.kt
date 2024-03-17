package com.fly.credit.jiana.js

import android.webkit.WebView
import com.fly.credit.jiana.manage.UserInfoManage
import com.fly.credit.jiana.util.Cons
import com.fly.credit.jiana.web.AndroidCallBackJS

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
object InvokeForCreditBagUserInfo {
    //获取用户信息
    public fun invokeForCreditBagUserInfo(id: String,webView : WebView){

        AndroidCallBackJS.callBackJsSuccess(webView,id
            , Cons.INVOKEFORCREDITBAGUSERINFO
            , UserInfoManage.getUserInfoJson())
    }
}