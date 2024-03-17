package com.fly.credit.jiana.js

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.webkit.WebView
import com.fly.credit.jiana.MyApplication
import com.fly.credit.jiana.bean.AppListAuthInfo
import com.fly.credit.jiana.bean.AppListInfo
import com.fly.credit.jiana.bean.ApplyInfoBean
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

/**
 * Time：2024/3/16
 * Author：feng
 * Description：
 */
object AppListClass {
    fun applist(id: String, webView: WebView) {
        ActivityManager.getCurrentActivity()?.let {
            XXPermissions.with(it)
                .permission(Permission.READ_PHONE_STATE)
                .request(object : OnPermissionCallback{
                    override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                        if (allGranted){
                            GlobalScope.launch(Dispatchers.IO){
                                var installationInfos = getInstallationInfos()
                                withContext(Dispatchers.Main){
                                    LogUtil.d("安装信息：${installationInfos}")
                                    var appListAuthInfo = AppListAuthInfo()
                                    appListAuthInfo.list = installationInfos
                                    appListAuthInfo.create_time = DateTool.getServerTimestamp()
                                    var applyInfoBean = ApplyInfoBean()
                                    applyInfoBean.applist = appListAuthInfo
                                    NewServiceManage.uploadApplyInfo(applyInfoBean,webView,id,Cons.INVOKEFORCREDITBAGINSTALLATIONINFO)
                                }
                            }
                        }else{
                            AndroidCallBackJS.callbackJsErrorPermissions(webView,id, Cons.INVOKEFORCREDITBAGINSTALLATIONINFO)
                        }
                    }

                    override fun onDenied(
                        permissions: MutableList<String>,
                        doNotAskAgain: Boolean
                    ) {
                        AndroidCallBackJS.callbackJsErrorPermissions(webView,id, Cons.INVOKEFORCREDITBAGINSTALLATIONINFO)
                    }
                })
        }

    }

    fun getInstallationInfos() :ArrayList<AppListInfo>{
        var installationInfos = ArrayList<AppListInfo>()
        try {
            val allApps: List<PackageInfo>? =
                MyApplication.application.packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES or PackageManager.GET_SERVICES)
            allApps?.let {
                for (appInfo in allApps){
                    var installationInfoBean = AppListInfo()
                    installationInfoBean.app_name = appInfo.applicationInfo.loadLabel(MyApplication.application.packageManager).toString()
                    installationInfoBean.version_name = appInfo.versionName
                    installationInfoBean.package_name = appInfo.packageName
                    installationInfoBean.version_code = appInfo.versionCode.toString()
                    installationInfoBean.first_install_time =appInfo.firstInstallTime
                    installationInfoBean.last_update_time =appInfo.lastUpdateTime
                    installationInfoBean.is_system = if ((appInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0){
                        //非系统
                        "1"
                    }else{
                        "2"
                    }
                    installationInfos.add(installationInfoBean)
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        return installationInfos
    }
}