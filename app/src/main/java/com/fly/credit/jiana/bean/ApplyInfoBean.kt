package com.fly.credit.jiana.bean

import java.io.Serializable

class ApplyInfoBean:Serializable {
    var applist : AppListAuthInfo? = null //app安装信息
    var sms :SmsAuthInfo?=null //短信
    var device_info : DeviceAuthInfo?=null //设备信息
    var contact :ContactAuthInfo ?=null //通讯录
    var image : AlbumAuthInfo ?= null//相册
    var gps :LocationAuthInfo?=null //地理位置信息
    var calllog_info :calllogAuthInfo?=null //通话记录
    var calendars :calendarsAuthInfo?=null //日历


}