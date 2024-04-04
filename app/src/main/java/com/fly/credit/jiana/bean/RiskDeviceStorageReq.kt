package com.fly.credit.jiana.bean

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
class RiskDeviceStorageReq: java.io.Serializable {

    //手机内部存储可用大小
    var  availableDiskSize: String? = ""

    //手机内部存储可用大小
    var  availableMemory: String? = ""

    //从开机到现在的毫秒数(包括睡眠时间)
    var  elapsedRealtime: Long? = 0

    //是否开启 USB 调试
    var  isUSBDebug: String? = ""

    //是否使用代理
    var  isUsingProxyPort: String? = ""

    //是否使用VPN
    var  isUsingVPN: String? = ""

    //手机内部存储总大小
    var  memorySize: String? = ""

    //总内存大小
    var  ram_total_size: String? = ""

    //内存使用大小
    var ramUsedSize: String? = ""

    //sd_card 使用了的内存大小
    var  totalDiskSize: String? = ""

}