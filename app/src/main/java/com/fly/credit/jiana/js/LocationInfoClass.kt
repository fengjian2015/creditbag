package com.fly.credit.jiana.js

import android.annotation.SuppressLint
import android.content.Context
import android.location.*
import android.os.Bundle
import android.text.TextUtils
import android.webkit.WebView
import com.fly.credit.jiana.MyApplication
import com.fly.credit.jiana.bean.ApplyInfoBean
import com.fly.credit.jiana.bean.LocationAuthInfo
import com.fly.credit.jiana.bean.LocationInfo
import com.fly.credit.jiana.network.NewServiceManage
import com.fly.credit.jiana.util.*
import com.fly.credit.jiana.web.AndroidCallBackJS
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.math.BigDecimal
import java.util.*

/**
 * Time：2024/3/16
 * Author：feng
 * Description：
 */
object LocationInfoClass {
    fun locationInfo(webView: WebView, id: String) {
        DeviceInfoUtil.openLocService()
        ActivityManager.getCurrentActivity()?.let {
            XXPermissions.with(it)
                .permission(Permission.ACCESS_COARSE_LOCATION)
                .permission(Permission.READ_PHONE_STATE)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                        if (allGranted){
                            initLocationListener()
                            if (!DeviceInfoUtil.isLocServiceEnable()) {
                                AndroidCallBackJS.callbackJsErrorPermissions(webView,id, Cons.INVOKEFORCREDITBAGLOCATIONINFO)
                                return
                            }
                            GlobalScope.launch(Dispatchers.IO){
                                var locationBean = LocationAuthInfo()
                                var location = getLocation()
//                                if (location == null) {
//                                    Thread.sleep(5000L)
//                                }
                                if (location == null){
                                    for (i in 0 .. 2){

                                        try {
                                            Thread.sleep(2000)
                                        }catch (e:Exception){
                                            e.printStackTrace()
                                        }
                                        location = getLastKnownLocation()
                                    }
                                }
                                locationBean.create_time = DateTool.getServerTimestamp()
                                var gps = LocationInfo()
                                location?.let {
                                    gps.latitude = it.latitude.toString()
                                    gps.longitude = it.longitude.toString()
                                    locationBean.device_id = DeviceUtils.getAndroidID()
                                    locationBean.gps = gps
                                    val address = getAddress(location.latitude, location.longitude)
                                    address?.let {
                                        locationBean.gps_address_province =address.adminArea
                                        locationBean.gps_address_city = address.locality
                                        if (TextUtils.isEmpty(address.featureName)) {
                                            address.featureName = address.getAddressLine(0)
                                        }
                                        if (TextUtils.isEmpty(address.featureName)) {
                                            address.featureName = address.subAdminArea
                                        }
                                        if (TextUtils.isEmpty(address.featureName)) {
                                            address.featureName = address.thoroughfare
                                        }
                                        if (TextUtils.isEmpty(address.thoroughfare)) {
                                            address.thoroughfare = address.featureName
                                        }
                                        locationBean.gps_address_street = address.thoroughfare
                                        locationBean.gps_address_address =address.featureName
                                    }
                                }
                                withContext(Dispatchers.Main){
                                    locationBean.gps?.latitude =
                                        locationBean.gps?.latitude?.let { it1 ->
                                            roundDown(it1,2)?.let { it1 ->
                                                toDouble(
                                                    it1
                                                ).toString()
                                            }
                                        }
                                    locationBean.gps?.longitude =
                                        locationBean.gps?.longitude?.let { it1 ->
                                            roundDown(it1,2)?.let { it1 ->
                                                toDouble(
                                                    it1
                                                ).toString()
                                            }
                                        }
                                    LogUtil.d("地址信息：$locationBean")
                                    var applyInfoBean = ApplyInfoBean()
                                    applyInfoBean.gps = locationBean
                                    NewServiceManage.uploadApplyInfo(applyInfoBean,webView,id,Cons.INVOKEFORCREDITBAGLOCATIONINFO)
                                }
                            }
                        }else{
                            AndroidCallBackJS.callbackJsErrorPermissions(webView,id, Cons.INVOKEFORCREDITBAGLOCATIONINFO)
                        }
                    }

                    override fun onDenied(
                        permissions: MutableList<String>,
                        doNotAskAgain: Boolean
                    ) {
                        AndroidCallBackJS.callbackJsErrorPermissions(webView,id, Cons.INVOKEFORCREDITBAGLOCATIONINFO)

                    }
                })
        }
    }

    fun getLocationManager(): LocationManager? {
        MyApplication.application?.let {
            if (locationManager == null) {
                locationManager =
                    it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            }
            return locationManager
        }

        return null
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(): Location? {
        getLocationManager()?.let {
            var lastLocation: Location? = null
            try {
                var l: Location? = it.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                if (l != null) {
                    return l
                }
                val providers: List<String> = it.getProviders(true)
                for (provider in providers) {
                    l = it.getLastKnownLocation(provider)
                    if (l == null) {
                        continue
                    }
                    if (lastLocation == null || l.accuracy < lastLocation.accuracy) {
                        lastLocation = l
                    }
                }
                return lastLocation

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return null
    }

    /**
     * 根据经纬度获取地理位置
     * @param latitude  纬度
     * @param longitude 经度
     * @return [Address]
     */
    fun getAddress(latitude: Double, longitude: Double): Address? {
        val geocoder = Geocoder(MyApplication.application, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 5)
            if (addresses != null) {
                if (addresses.size > 0) return addresses[0]
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun roundDown(v1: Any, scale: Int): String? {
        var result = "0"
        require(scale >= 0) { "The scale must be a positive integer or zero" }
        try {
            val b1 = BigDecimal(v1.toString())
            val b2 = BigDecimal(1)
            result = b1.divide(b2, scale, BigDecimal.ROUND_DOWN).toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    fun toDouble(str: String): Double {
        try {
            return str.toDouble()
        } catch (e: java.lang.Exception) {
        }
        return 0.0
    }


    fun getLocation(): Location? {
        var location: Location? = null
        val locationManager =
            MyApplication.application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager?.let {
            val providers = locationManager.getProviders(true)
            for (provider in providers) {
                @SuppressLint("MissingPermission")
                val l = locationManager.getLastKnownLocation(provider!!) ?: continue
                if (location == null || l.accuracy < location!!.accuracy) {
                    location = l
                }
            }
        }
        return location
    }

    private val mLocationListener: LocationListener = object : LocationListener {
        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            LogUtil.d("onStatusChanged")
        }

        // Provider被enable时触发此函数，比如GPS被打开
        override fun onProviderEnabled(provider: String) {
            LogUtil.d("onProviderEnabled")
        }

        // Provider被disable时触发此函数，比如GPS被关闭
        override fun onProviderDisabled(provider: String) {
            LogUtil.d("onProviderDisabled")
        }

        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        override fun onLocationChanged(location: Location) {
            LogUtil.d(
                String.format(
                    "location: longitude: %f, latitude: %f", location.longitude,
                    location.latitude
                )
            )
            //更新位置信息
            locationManager!!.removeUpdates(this)
        }
    }

    private var locationManager: LocationManager? = null

    /**
     * 监听位置变化
     */
    @SuppressLint("MissingPermission")
    fun initLocationListener() {
        locationManager = MyApplication.application.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        if (locationManager == null) {
            return
        }
        locationManager!!.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            1000,
            10f,
            mLocationListener
        )
    }
}