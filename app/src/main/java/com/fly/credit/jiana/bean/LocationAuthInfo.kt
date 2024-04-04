package com.fly.credit.jiana.bean

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
class LocationAuthInfo : java.io.Serializable {
    //抓取时间 13位时间戳
    var create_time: Long? = null

    //gps经纬度
    var gps: LocationInfo? = null

    //gps解析出来的省
    var gps_address_province: String? = ""

    //gps解析出来的城市
    var gps_address_city: String? = ""

    //gps解析的地址
    var gps_address_street: String? = ""

    //gps解析的具体地址
    var gps_address_address: String? = ""

    //新增
    //设备id
    var device_id: String? = null
    override fun toString(): String {
        return "LocationAuthInfo(create_time=$create_time, gps=$gps, gps_address_province=$gps_address_province, gps_address_city=$gps_address_city, gps_address_street=$gps_address_street, gps_address_address=$gps_address_address, device_id=$device_id)"
    }

}