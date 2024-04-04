package com.fly.credit.jiana.bean

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
class LocationInfo : java.io.Serializable {
    //gps维度
    var latitude: String? = ""

    //gps经度
    var longitude: String? = ""
    override fun toString(): String {
        return "LocationInfo(latitude=$latitude, longitude=$longitude)"
    }
}