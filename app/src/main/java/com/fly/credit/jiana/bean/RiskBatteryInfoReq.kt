package com.fly.credit.jiana.bean

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
class RiskBatteryInfoReq : java.io.Serializable {
    //是否 usb 充电，0：否，1：是
    var is_usb_charge = 0

    //是否交流充电，0：否，1：是
    var is_ac_charge = 0

    //string	电池百分比
    var batteryPercentage: String? = ""

    //电池状态
    var battery_temper: String? = ""

    //电池寿命
    var battery_health: String? = ""

    //是否正在充电，UNKNOWN： 1，CHARGING：2，DISCHARGING：3
    var batteryStatus: String? = ""

    //新增
    //获取电量的时间 到毫秒
    var power_time: Long = 0

    //设备ID
    var device_id: String? = ""

    //电池电压
    var voltage: String? = ""

    //获取电池容量mAh  （单位mAh）
    var battery_capacity: String? = ""

}