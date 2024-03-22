package com.fly.credit.jiana.bean

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
class RiskDeviceGeneralReq : java.io.Serializable {

    //指示设备电话类型的常量 NONE：0，GS：1，CDMA：2，SIP=3"language
    var phone_type: String? = null

    //系统语言
    var language: String? = null

    //用户显示的语言环境语音的名称
    var locale_display_language: String? = null
    var locale_display_name : String? = null

    //网络运营商名称	TELCEL
    var network_operator_name: String? = null

    //此地区的国家地区缩写
    var locale_iso_3_country: String? = null

    //语言环境的字母缩写	eng
    var locale_iso_3_language: String? = null

    //新增
    //最后一次启动时间，毫秒时间戳
    var last_boot_time: String? = null

    //是否模拟器(0/1)
    var is_simulator: String? = null

}