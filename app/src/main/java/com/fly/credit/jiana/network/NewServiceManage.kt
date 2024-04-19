package com.fly.credit.jiana.network

import android.os.Build
import android.util.Base64
import android.webkit.WebView
import com.facebook.FacebookSdk
import com.fly.credit.jiana.MyApplication
import com.fly.credit.jiana.bean.*
import com.fly.credit.jiana.js.LogoutClass
import com.fly.credit.jiana.manage.FaceBookManage
import com.fly.credit.jiana.manage.UserInfoManage
import com.fly.credit.jiana.util.*
import com.fly.credit.jiana.util.Cons.INSTALL_REFERRER_RESPONSE_JSON
import com.fly.credit.jiana.util.Cons.KEY_PROTOCAL_1
import com.fly.credit.jiana.util.Cons.KEY_PROTOCAL_2
import com.fly.credit.jiana.web.AndroidCallBackJS
import com.google.gson.Gson

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
object NewServiceManage {

    fun getProtocolUrlv2(){
        val map: MutableMap<String, String> = HashMap()
        NetClient.getNewService().getProtocolUrlv2(map)
            .compose(NetUtil.applySchedulers())
            .subscribe(object : NetCallback<ProtocolUrlInfoList?>() {
                override fun businessFail(netErrorModel: NetErrorModel) {}

                override fun businessSuccess(data: ProtocolUrlInfoList?) {
                    if (data?.code == 200){
                        data.data?.let {
                            for (protocolUrlBean in it){
                                LogUtil.d("----:"+protocolUrlBean.url)
                                if (protocolUrlBean.protocalType == 1){
                                    MMKVCacheUtil.putString(KEY_PROTOCAL_1,protocolUrlBean.url)
                                } else if (protocolUrlBean.protocalType == 2){
                                    MMKVCacheUtil.putString(KEY_PROTOCAL_2,protocolUrlBean.url)
                                }
                            }
                        }
                    }
                }
            })
    }

    fun login(phone: String, code: String, function: Function1<Int,Int>){
        val map: MutableMap<String, String> = HashMap()
        map["Phone"] = phone
        map["Code"] = code
        map["mobilePhoneBrands"] = Build.BRAND
        map["appMarketCode"] = "Google"
        map["deviceModel"] = Build.MODEL
        map["version"] = DeviceInfoUtil.getVerName(MyApplication.application)
        map["client"] = "android"
        map["clientVersion"] = Build.DISPLAY
        map["extension"] = MMKVCacheUtil.getString(INSTALL_REFERRER_RESPONSE_JSON)
        map["adjustId"] = FacebookSdk.getApplicationId()

        NetClient.getNewService()
            .loginByPhoneVerifyCode(map)
            .compose(NetUtil.applySchedulers())
            .doOnSubscribe { LoadingUtil.show() }
            .doFinally { LoadingUtil.dismiss() }
            .subscribe(object : NetCallback<LoginlInfoListBase?>() {
                override fun businessFail(netErrorModel: NetErrorModel) {
                    function.invoke(2)
                    ToastUtil.show(netErrorModel.message)
                }

                override fun businessSuccess(data: LoginlInfoListBase?) {
                    if (data?.code == 200){
                        data?.data?.let {
                            if (it.isNew){
                                addUserAction()
                            }
                            var userInfoBean = UserInfoBean()
                            userInfoBean.isNew = it.isNew
                            userInfoBean.homeUrl = it.homeUrl
                            userInfoBean.phone = it.phone
                            userInfoBean.name = it.name
                            userInfoBean.token = it.token
                            userInfoBean.appInstallUrl = it.appInstallUrl
                            userInfoBean.mustupdate = it.mustupdate
                            UserInfoManage.saveUserInfo(userInfoBean)
                            if (userInfoBean.mustupdate!=null && userInfoBean.mustupdate!!.equals(1)){
                                function.invoke(3)
                            }else {
                                function.invoke(1)
                            }
                        }
                    }else{
                        ToastUtil.show(data?.message)
                        function.invoke(2)
                    }
                }
            })
    }

    fun addUserAction(){
        FaceBookManage.faceLog("CompleteRegistration")
        val map: MutableMap<String, String> = HashMap()
        map["start_time"] = DateTool.getTimeFromLongYMDHMS(DateTool.getServerTimestamp())!!
        map["end_time"] = DateTool.getTimeFromLongYMDHMS(DateTool.getServerTimestamp())!!
        map["scene_type"] = "1"
        map["device_no"] = DeviceUtils.getAndroidID()
        NetClient.getNewService().addUserAction(map)
            .compose(NetUtil.applySchedulers())
            .subscribe(object : NetCallback<BaseResponseBean?>() {
                override fun businessFail(netErrorModel: NetErrorModel) {

                }

                override fun businessSuccess(data: BaseResponseBean?) {

                }
            })
    }

    fun checkUpdate(function: Function1<Int,Int>){
        val map: MutableMap<String, String> = HashMap()
        NetClient.getNewService()
            .staticLoginv2(map)
            .compose(NetUtil.applySchedulers())
            .subscribe(object : NetCallback<LoginlInfoListBase?>() {
                override fun businessFail(netErrorModel: NetErrorModel) {
                    LogoutClass.logout()
                }

                override fun businessSuccess(data: LoginlInfoListBase?) {
                    if (data?.code == 200){
                        data?.data?.let {
                            var userInfoBean = UserInfoBean()
                            userInfoBean.isNew = it.isNew
                            userInfoBean.homeUrl = it.homeUrl
                            userInfoBean.phone = it.phone
                            userInfoBean.name = it.name
                            userInfoBean.token = it.token
                            userInfoBean.appInstallUrl = it.appInstallUrl
                            userInfoBean.mustupdate = it.mustupdate
                            UserInfoManage.saveUserInfo(userInfoBean)
                            if (userInfoBean.mustupdate!=null && userInfoBean.mustupdate!!.equals(1)){
                                function.invoke(3)
                            }
                        }
                    }
                }
            })
    }

    fun sendVerifyCode(phone : String,function: Function1<Boolean,Boolean>){
        val map: MutableMap<String, String> = HashMap()
        map["phone"] = phone
        NetClient.getNewService().sendVerifyCode(map)
            .compose(NetUtil.applySchedulers())
            .doOnSubscribe { LoadingUtil.show() }
            .doFinally { LoadingUtil.dismiss() }
            .subscribe(object : NetCallback<BaseResponseBean?>() {
                override fun businessFail(netErrorModel: NetErrorModel) {
                    function.invoke(false)
                    ToastUtil.show(netErrorModel?.message)
                }

                override fun businessSuccess(data: BaseResponseBean?) {
                    if (data?.code == 200){
                        function.invoke(true)
                        ToastUtil.show(data?.message)
                    }else{
                        function.invoke(false)
                        ToastUtil.show(data?.message)
                    }
                }
            })
    }

    /**
     * 上传风控数据
     */
    fun uploadApplyInfo(applyInfoBean:ApplyInfoBean, mWebView: WebView, id:String, event:String){
        val map: MutableMap<String, String> = HashMap()
        val content = Gson().toJson(applyInfoBean)
        map["authInfo"] = Base64.encodeToString(content.toByteArray(), Base64.DEFAULT)
        NetClient.getNewService().uploadCreditModeLoanWardAuth(map)
            .compose(NetUtil.applySchedulers())
            .subscribe(object : NetCallback<BaseResponseBean>() {
                override fun businessSuccess(data: BaseResponseBean) {
                    if (data.code == 200){
                        AndroidCallBackJS.callBackJsSuccess(mWebView,id,event)
                    }else{
                        AndroidCallBackJS.callbackJsErrorOther(mWebView,id,event,data.message)
                    }
                }

                override fun businessFail(netErrorModel: NetErrorModel?) {
                    if (netErrorModel == null){
                        AndroidCallBackJS.callbackJsErrorPermissions(mWebView,id,event)
                    }else{
                        AndroidCallBackJS.callbackJsErrorOther(mWebView,id,event,netErrorModel.message)
                    }
                }
            })
    }
}