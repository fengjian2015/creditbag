package com.fly.credit.jiana.js

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.content.ContentResolver
import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Environment
import android.os.SystemClock
import android.provider.Settings
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import android.util.Size
import android.webkit.WebView
import com.fly.credit.jiana.MyApplication
import com.fly.credit.jiana.bean.*
import com.fly.credit.jiana.network.NewServiceManage
import com.fly.credit.jiana.util.*
import com.fly.credit.jiana.util.Cons.INVOKEFORCREDITBAGDEVICEINFO
import com.fly.credit.jiana.web.AndroidCallBackJS
import com.google.gson.Gson
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Method
import java.util.*

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
object DeviceInfoClass {
    fun deviceInfo(mContext : Context,id: String, webView: WebView, data: Any?) {

        SpecialPermissionUtil.openLocService()
        SpecialPermissionUtil.openWifi()
        XXPermissions.with(mContext)
            .permission(Permission.ACCESS_FINE_LOCATION)
            .permission(Permission.ACCESS_COARSE_LOCATION)
            .permission(Permission.READ_PHONE_STATE)
            .permission(Permission.GET_ACCOUNTS)
            .permission(Permission.READ_MEDIA_AUDIO)
            .permission(Permission.READ_MEDIA_IMAGES)
            .permission(Permission.READ_MEDIA_VIDEO)
            .permission(Permission.READ_CONTACTS)
            .request(object : OnPermissionCallback{
                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    if (allGranted) {
                        if (!SpecialPermissionUtil.isLocServiceEnable() || !SpecialPermissionUtil.isOpenWifi()) {
                            AndroidCallBackJS.callbackJsErrorPermissions(webView,id,INVOKEFORCREDITBAGDEVICEINFO);
                        } else {
                            GlobalScope.launch(Dispatchers.IO){
                                LogUtil.d("需要等待ip,开启WiFi后需要等待一段时间才可进行抓取")
                                Thread.sleep(3000)
                                var commentParseDataBean =
                                    Gson().fromJson(data.toString(), CopyBean::class.java)
                                var deviceAuthInfo = DeviceAuthInfo();
                                deviceAuthInfo.create_time = DateTool.getServerTimestamp()
                                deviceAuthInfo.VideoExternal = FileUtil.getVideoExternalFiles().size

                                var riskDeviceStorageReq = RiskDeviceStorageReq()
                                riskDeviceStorageReq.availableDiskSize= DeviceInfoUtil.getAvailableSpace()
                                riskDeviceStorageReq.availableMemory = DeviceInfoUtil.getAvailMemory()?.toString()
                                riskDeviceStorageReq.elapsedRealtime = SystemClock.elapsedRealtime()
                                riskDeviceStorageReq.isUSBDebug = DeviceInfoUtil.isUsbDebug()
                                riskDeviceStorageReq.isUsingProxyPort = DeviceInfoUtil.isProxy()
                                riskDeviceStorageReq.isUsingVPN = DeviceInfoUtil.isVpn()
                                riskDeviceStorageReq.memorySize =DeviceInfoUtil.getTotalMemory()?.toString()
                                riskDeviceStorageReq.ram_total_size = DeviceInfoUtil.getTotalMemory()?.toString()
                                riskDeviceStorageReq.totalDiskSize =DeviceInfoUtil.getTotalRam()
                                deviceAuthInfo.storage = riskDeviceStorageReq

                                var d = RiskDeviceGeneralReq();
                                d.phone_type = DeviceInfoUtil.getPhoneType()
                                d.language = MyApplication.application.resources.configuration.locale.language
                                d.locale_display_language = Locale.getDefault().toString()
                                d.network_operator_name = DeviceInfoUtil.getOperatorName()
                                d.locale_iso_3_country = MyApplication.application.resources.configuration.locale.isO3Country
                                d.locale_iso_3_language =MyApplication.application.resources.configuration.locale.isO3Language
                                d.last_boot_time = (System.currentTimeMillis() - SystemClock.elapsedRealtime()).toString()
                                d.is_simulator = if (DeviceUtils.isDeviceRooted()) "true" else "false"
                                deviceAuthInfo.general_data = d

                                var riskHardwareReq = RiskHardwareReq();
                                riskHardwareReq.device_model = Build.MODEL
                                riskHardwareReq.imei = DeviceInfoUtil.getIMEI()
                                riskHardwareReq.sys_version = Build.VERSION.SDK_INT.toString()
                                riskHardwareReq.screenResolution = DeviceInfoUtil.getScreenResolution()
                                riskHardwareReq.manufacturerName = Build.BRAND
                                riskHardwareReq.screenxpx = DeviceInfoUtil.getScreenResolutionWidth()
                                riskHardwareReq.screenypx = DeviceInfoUtil.getScreenResolutionHeight()
                                riskHardwareReq.is_xposed_or_root = if (DeviceUtils.hasXposed(MyApplication.application)) "true" else "false"
                                riskHardwareReq.cpu_frequency = DeviceUtils.getCPUMaxFreq();
                                riskHardwareReq.physical_size = DeviceInfoUtil.getPhysicalSize()
                                riskHardwareReq.cpu_model = DeviceInfoUtil.getCpuName()
                                riskHardwareReq.keyboard = DeviceInfoUtil.onEvaluateInputViewShown()
                                riskHardwareReq.device_no = DeviceInfoUtil.getGAID()
                                deviceAuthInfo.hardware = riskHardwareReq

                                var riskDevicePublicIPReq= RiskDevicePublicIPReq()
                                riskDevicePublicIPReq.first_ip = commentParseDataBean.ip
                                deviceAuthInfo.public_ip = riskDevicePublicIPReq

                                var riskBatteryInfoReq = RiskBatteryInfoReq()
                                riskBatteryInfoReq.is_usb_charge =MMKVCacheUtil.getInt(Cons.KEY_BATTERY_IS_USB)
                                riskBatteryInfoReq.is_ac_charge =MMKVCacheUtil.getInt(Cons.KEY_BATTERY_IS_AC)
                                riskBatteryInfoReq.batteryPercentage =MMKVCacheUtil.getString(Cons.KEY_BATTERY_LEVEL)
                                riskBatteryInfoReq.battery_temper = MMKVCacheUtil.getInt(Cons.KEY_BATTERY_TEMPER).toString()
                                riskBatteryInfoReq.battery_health =MMKVCacheUtil.getInt(Cons.KEY_BATTERY_HEALTH).toString()
                                riskBatteryInfoReq.batteryStatus = MMKVCacheUtil.getInt(Cons.KEY_BATTERY_STATUS).toString()
                                riskBatteryInfoReq.power_time = DateTool.getServerTimestamp()
                                riskBatteryInfoReq.device_id = DeviceUtils.getAndroidID()
                                riskBatteryInfoReq.voltage = MMKVCacheUtil.getInt(Cons.KEY_BATTERY_VOLTAGE).toString()
                                riskBatteryInfoReq.battery_capacity =DeviceInfoUtil.getCounter();
                                deviceAuthInfo.battery_status = riskBatteryInfoReq

                                deviceAuthInfo.phone_brand =Build.BRAND
                                deviceAuthInfo.cur_wifi_mac =DeviceInfoUtil.getWifiInfo()
                                deviceAuthInfo.imei2 = DeviceInfoUtil.getIMEI1().toString()
                                deviceAuthInfo.imei1 = DeviceInfoUtil.getIMEI1().toString()
                                deviceAuthInfo.build_fingerprint =Build.FINGERPRINT
                                deviceAuthInfo.cur_wifi_ssid =DeviceInfoUtil.getWifiName()
                                deviceAuthInfo.DownloadFiles = FileUtil.getDownloadFiles().size
                                deviceAuthInfo.time_zoneId =DeviceInfoUtil.getTimeZoneId()
                                deviceAuthInfo.kernel_version =DeviceInfoUtil.getKernelVersion()
                                deviceAuthInfo.currentSystemTime = (System.currentTimeMillis() / 1000).toString()
                                deviceAuthInfo.AudioInternal= FileUtil.getAudioInternalFiles().size.toString()
                                deviceAuthInfo.nettype = DeviceInfoUtil.getNetworkState().toString()
                                deviceAuthInfo.serial =Build.SERIAL
                                deviceAuthInfo.android_id =DeviceUtils.getAndroidID()
                                deviceAuthInfo.kernel_architecture=Build.CPU_ABI
                                deviceAuthInfo.build_id =Build.ID
                                deviceAuthInfo.ImagesInternal= FileUtil.getImagesInternalFiles().size.toString()
                                deviceAuthInfo.API_level =Build.VERSION.SDK_INT
                                deviceAuthInfo.build_number=Build.DISPLAY
                                deviceAuthInfo.ContactGroup = AuthDataUtil.getAllGroupInfo().size.toString()
                                deviceAuthInfo.mac =DeviceUtils.getMacAddress()

                                var riskDeviceInfoReq = RiskDeviceInfoReq()
                                riskDeviceInfoReq.isRooted =
                                    if (DeviceUtils.isDeviceRooted()) "true" else "false"
                                deviceAuthInfo.device_info =riskDeviceInfoReq
                                deviceAuthInfo.gsfid =getGsfAndroidId()
                                deviceAuthInfo.board =Build.BOARD
                                deviceAuthInfo.VideoInternal =FileUtil.getVideoInternalFiles().size
                                deviceAuthInfo.AudioExternal = FileUtil.getAudioExternalFiles().size
                                deviceAuthInfo.build_time = Build.TIME
                                deviceAuthInfo.wifiCount =DeviceInfoUtil.getWifiList().size
                                deviceAuthInfo.time_zone =DeviceInfoUtil.getTimeZone()
                                deviceAuthInfo.release_date =Build.TIME
                                deviceAuthInfo.iccid2 =getICCID2()
                                deviceAuthInfo.device_name =DeviceInfoUtil.getDeviceName()
                                deviceAuthInfo.ImagesExternal = FileUtil.getImagesExternalFiles().size.toString()
                                deviceAuthInfo.security_patch_level =Build.VERSION.SECURITY_PATCH
                                deviceAuthInfo.sensorCount =DeviceInfoUtil.getSensorCount()
                                deviceAuthInfo.wifiList =DeviceInfoUtil.getWifiList()
                                deviceAuthInfo.wifi_state =DeviceInfoUtil.getCurrentNetworkRssi()
                                deviceAuthInfo.gaid =DeviceInfoUtil.getGAID()
                                deviceAuthInfo.back_num =ActivityManager.back_num
                                deviceAuthInfo.open_time =DateTool.getTimeFromLongYMDHMS(MyApplication.appStartTime)
                                deviceAuthInfo.complete_time =DateTool.getTimeFromLongYMDHMS(DateTool.getServerTimestamp())
                                deviceAuthInfo.open_power =MyApplication.open_power
                                deviceAuthInfo.complete_apply_power= MyApplication.complete_apply_power
                                deviceAuthInfo.telephony= getCarrierName(MyApplication.application)
                                deviceAuthInfo.network_status = getNetworkState(MyApplication.application)
                                deviceAuthInfo.dbm = MyApplication.dbm.toString()
                                deviceAuthInfo.is_contain_sd = DeviceInfoUtil.checkSDK().toString()
                                deviceAuthInfo.sensors = getSensorBeanList()
                                deviceAuthInfo.wifiInfos = getWifiList()
                                deviceAuthInfo.front_camera_pixels =getFrontCameraResolution(MyApplication.application)
                                deviceAuthInfo.screen_brightness = getScreenBrightness(MyApplication.application).toString()
                                deviceAuthInfo.sim_card_type = getSimCardType(MyApplication.application)
                                deviceAuthInfo.silent_mode = getPhoneMode(MyApplication.application).toString()
                                deviceAuthInfo.rear_camera_pixels = getBackCameraResolution(MyApplication.application)
                                deviceAuthInfo.network_operator_country_code = getNetworkOperatorCountryCode(MyApplication.application)
                                deviceAuthInfo.network_operator_code  = getSimOperator(MyApplication.application)
                                deviceAuthInfo.locale_country =Locale.getDefault().country
                                deviceAuthInfo.locale_display_country = Locale.getDefault().displayCountry
                                deviceAuthInfo.airplane_mode = isFlightModeEnabled(MyApplication.application)
                                deviceAuthInfo.bluetooth_flag = getBluetoothEnable(MyApplication.application).toString()
                                if (isDualSimSupported(MyApplication.application)){
                                    deviceAuthInfo.is_dualcard_standby = "1"
                                } else{
                                    deviceAuthInfo.is_dualcard_standby = "0"
                                }
                                if (isHotSpotApOpen2(MyApplication.application)){
                                    deviceAuthInfo.wifi_hotspot ="1"
                                }else{
                                    deviceAuthInfo.wifi_hotspot ="2"
                                }
                                if (isExternalStorageAvailable(MyApplication.application)){
                                    deviceAuthInfo.is_insert_expansion_card = "1"
                                } else{
                                    deviceAuthInfo.is_insert_expansion_card = "0"
                                }
                                deviceAuthInfo.version_number = getVersionCode(MyApplication.application).toString()
                                deviceAuthInfo.version_name = getVersionName(MyApplication.application)
                                deviceAuthInfo.network_operator_name =getNetworkOperatorName(MyApplication.application)
                                deviceAuthInfo.basebandVer =Build.getRadioVersion()
                                withContext(Dispatchers.Main){
                                    LogUtil.d("设备信息获取完成：$deviceAuthInfo")
                                    var applyInfoBean = ApplyInfoBean()
                                    applyInfoBean.device_info = deviceAuthInfo
                                    NewServiceManage.uploadApplyInfo(applyInfoBean,webView,id,Cons.INVOKEFORCREDITBAGDEVICEINFO)
                                }
                            }
                        }

                    }
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    AndroidCallBackJS.callbackJsErrorPermissions(webView,id,INVOKEFORCREDITBAGDEVICEINFO);
                }
            })
    }


    /**
     * BASEBAND-VER
     * 基带版本
     * return String
     */
    fun getBasebandVersion(): String {
        var Version = ""
        try {
            val cl = Class.forName("android.os.SystemProperties")
            val invoker = cl.newInstance()
            val m = cl.getMethod("get", *arrayOf<Class<*>>(String::class.java, String::class.java))
            val result = m.invoke(invoker, *arrayOf<Any>("gsm.version.baseband", "no message"))
            Version = result as String
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Version
    }

    fun getNetworkOperatorName(context: Context): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.networkOperatorName
    }

    fun getVersionName(context: Context?): String {
        if (context == null) {
            return ""
        }
        var versionName = ""
        try {
            versionName =
                context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return versionName
    }

    fun getVersionCode(context: Context?): Int {
        var code = 0
        if (context == null) {
            return code
        }
        try {
            code = context.packageManager.getPackageInfo(context.packageName, 0).versionCode
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return code
    }

    fun isExternalStorageAvailable(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            isExternalStorageAvailableQ(context)
        } else {
            isExternalStorageAvailableLegacy()
        }
    }

    private fun isExternalStorageAvailableLegacy(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

    private fun isExternalStorageAvailableQ(context: Context): Boolean {
        val externalDirs = context.getExternalFilesDirs(null)
        for (externalDir in externalDirs) {
            if (externalDir != null && Environment.MEDIA_MOUNTED == Environment.getStorageState(externalDir)) {
                return true
            }
        }
        return false
    }

    //获取热点是否打开方式2
    fun isHotSpotApOpen2(context: Context): Boolean {
        var isAPEnable = false
        try {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val method: Method = wifiManager.javaClass.getDeclaredMethod("isWifiApEnabled")
            method.setAccessible(true)
            isAPEnable = method.invoke(wifiManager) as Boolean
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return isAPEnable
    }

    fun isDualSimSupported(context: Context): Boolean {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        // 在API Level 22及以上，可以使用SubscriptionManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val subscriptionManager =
                context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

            val activeSubscriptionInfoList: List<SubscriptionInfo> =
                subscriptionManager.activeSubscriptionInfoList ?: emptyList()

            return activeSubscriptionInfoList.size >= 2
        }

        // 在API Level 22以下，一些制造商可能提供特定的方法来检查
        // 例如：Samsung设备上可以使用 telephonyManager.getPhoneCount() 来判断双卡支持

        // 默认返回false
        return false
    }

    fun getBluetoothEnable(context: Context?):Int{
        try {
            context?.let {
                val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                val adapter = manager.adapter
                if (adapter != null){
                    if (adapter.isEnabled){
                        return 1
                    } else{
                        return 2
                    }
                }

            }
        }catch (e:Exception){
            e.printStackTrace()
        }

        return 0
    }

    // 判断飞行模式是否开启
    fun isFlightModeEnabled(context: Context): String {
        val state = Settings.Global.getInt(context.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON, 0) != 0

        return if (state){
            "1"
        } else{
            "0"
        }

    }

    fun getSimOperator(context: Context): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.simOperator
    }

    fun getNetworkOperatorCountryCode(context: Context): String {
        try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val simOperator = telephonyManager.simOperator

            // 获取 MCC 部分
            val mcc = getCountryCodeFromMCC(simOperator)

            // 返回国家编号
            return mcc
        }catch (e:Exception){
            e.printStackTrace()
        }
        return ""
    }
    fun getCountryCodeFromMCC(mcc: String): String {
        // MCC 格式通常为三位数字
        if (mcc.length >= 3) {
            return mcc.substring(0, 3)
        }
        return ""
    }

    fun getBackCameraResolution(context: Context): String {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try {
            for (cameraId in cameraManager.cameraIdList) {
                val characteristics = cameraManager.getCameraCharacteristics(cameraId)

                if (characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                    val streamConfigurationMap =
                        characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

                    val sizes: Array<Size>? = streamConfigurationMap?.getOutputSizes(android.graphics.ImageFormat.JPEG)
                    val largestSize = sizes?.maxByOrNull { it.width * it.height }

                    return if (largestSize != null) {
                        "${largestSize.width}x${largestSize.height}"
                    } else {
                        ""
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    fun getScreenBrightness(context: Context): Int {
        val contentResolver: ContentResolver = context.contentResolver
        return Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS)
    }

    fun getPhoneMode(context: Context): Int {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        return when (audioManager.ringerMode) {
            AudioManager.RINGER_MODE_NORMAL -> 1 // 普通模式
            AudioManager.RINGER_MODE_VIBRATE -> 2 // 振动模式
            AudioManager.RINGER_MODE_SILENT -> 3 // 静音模式
            else -> 0 // 未知
        }
    }

    fun getFrontCameraResolution(context: Context): String {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try {
            // 获取可用摄像头列表
            val cameraIdList = cameraManager.cameraIdList

            for (cameraId in cameraIdList) {
                val characteristics = cameraManager.getCameraCharacteristics(cameraId)

                // 判断摄像头是否为前置摄像头
                val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
                if (facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    // 获取前置摄像头分辨率
                    val streamConfigurationMap =
                        characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                    val sizes = streamConfigurationMap?.getOutputSizes(SurfaceTexture::class.java)


                    // 选择分辨率，这里选择第一个分辨率作为示例
                    val selectedSize = sizes?.get(0)

                    return "${selectedSize?.width}x${selectedSize?.height}"
                }
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

        return ""
    }

    fun getCarrierName(context: Context): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        // 获取运营商名称
        return when (telephonyManager.simState) {
            TelephonyManager.SIM_STATE_READY -> {
                // SIM卡准备就绪
                telephonyManager.networkOperatorName ?: "Unknown"
            }
            else -> {
                // SIM卡未就绪或不可用
                "Not Available"
            }
        }
    }

    fun getGsfAndroidId(): String? {
        try {
            val URI = Uri.parse("content://com.google.android.gsf.gservices")
            val ID_KEY = "android_id"
            val params = arrayOf(ID_KEY)
            val c = MyApplication.application.contentResolver.query(URI, null, null, params, null)
            return if (!c!!.moveToFirst() || c.columnCount < 2) null else try {
                var c1 = c.getString(1)
                var c0 = c.getString(0)
                c1?.let {
                    java.lang.Long.toHexString(c1.toLong())
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getICCID2(): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val iccids = getICCIDNumberAll(MyApplication.application)
            var iccid =""
            if (iccids.size >= 2) {
                iccid = iccids[1].toString()
            }
            iccid
        } else {
            ""
        }
        return ""
    }

    @SuppressLint("MissingPermission")
    fun getICCIDNumberAll(context: Context?): ArrayList<String?> {
        val sis: List<SubscriptionInfo>
        var iccids = ArrayList<String?>()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val sm = SubscriptionManager.from(context)
            val count = sm.activeSubscriptionInfoCount //当前实际插卡数量
            val max = sm.activeSubscriptionInfoCountMax //当前卡槽数量
            Log.d("card number=", count.toString())
            Log.d("card slot num", max.toString())
            sis = sm.activeSubscriptionInfoList
            for (subInfo in sis) {
                Log.d("iccId", subInfo.iccId) //ICC ID
                iccids.add(subInfo.iccId)
            }
            iccids
        } else {
            iccids
        }
    }
    /**
     * 获取当前网络连接的类型
     *
     * @param context context
     * @return int
     */
    @SuppressLint("MissingPermission")
    fun getNetworkState(context: Context): String {
        val connManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                ?: // 为空则认为无网络
                return "无网络" // 获取网络服务
        // 获取网络类型，如果为空，返回无网络
        val activeNetInfo = connManager.activeNetworkInfo
        if (activeNetInfo == null || !activeNetInfo.isAvailable) {
            return "无网络"
        }
        // 判断是否为WIFI
        val wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (null != wifiInfo) {
            val state = wifiInfo.state
            if (null != state) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return "wifi"
                }
            }
        }
        // 若不是WIFI，则去判断是2G、3G、4G网
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val networkType = telephonyManager.networkType
        return when (networkType) {
            TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> "2G"
            TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> "3G"
            TelephonyManager.NETWORK_TYPE_LTE -> "4G"
            TelephonyManager.NETWORK_TYPE_NR -> "5G"
            else -> "手机流量"
        }
    }


    fun getSensorList(): List<Sensor> {
        val sm =
            MyApplication.application.getSystemService(Context.SENSOR_SERVICE) as SensorManager //获取系统的传感器服务并创建实例
        return sm.getSensorList(Sensor.TYPE_ALL) //获取传感器的集合
    }

    fun getSensorBeanList(): ArrayList<RiskSensorReq> {
        var sensorListBeans = ArrayList<RiskSensorReq>()
        val sensorList = getSensorList()
        for (sensor in sensorList) {
            var sensorListBean = RiskSensorReq()
            sensorListBean.type = sensor.type
            sensorListBean.name = sensor.name
            sensorListBean.version = sensor.version.toString()
            sensorListBean.vendor = sensor.vendor
            sensorListBean.max_range = sensor.maximumRange.toString()
            sensorListBean.min_delay = sensor.minDelay.toString()
            sensorListBean.power = sensor.power.toString()
            sensorListBean.resolution = sensor.resolution.toString()
            sensorListBeans.add(sensorListBean)
        }
        return sensorListBeans
    }

    /**
     * wifi列表
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    fun getWifiList(): ArrayList<RiskWifiReq> {
        val wifiList: ArrayList<RiskWifiReq> = ArrayList()
        try {
            val wifiManager =
                MyApplication.application.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val scanWifiList = wifiManager.scanResults
            val curWifi = DeviceInfoUtil.getWifiName()
            scanWifiList?.let {
                for (i in scanWifiList.indices) {
                    var wifiListBean = RiskWifiReq()
                    val scanResult = scanWifiList[i]
                    if (!TextUtils.isEmpty(scanResult.SSID)) {
                        wifiListBean.wifi_name = scanResult.SSID
                        wifiListBean.bssid = scanResult.BSSID
                        wifiListBean.type = if (curWifi.equals(scanResult.SSID)) 1 else 2
                        wifiListBean.mac = "02:00:00:00"
                        wifiList.add(wifiListBean)
                    }
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
        return wifiList
    }

    const val CHINA_MOBILE = "China Mobile"
    const val CHINA_UNICOM = "China Unicom"
    const val CHINA_TELECOM = "China Telecom"
    const val OTHER = "Other"

    fun getSimCardType(context: Context): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val operator = telephonyManager.simOperator

        return when (operator) {
            "46000", "46002", "46007" -> CHINA_MOBILE
            "46001", "46006", "46009" -> CHINA_UNICOM
            "46003", "46005", "46011" -> CHINA_TELECOM
            else -> OTHER
        }
    }
}
