package com.fly.credit.jiana.bean

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
class RiskHardwareReq : java.io.Serializable {
    //型号
    var device_model: String? = null

    //设备号
    var imei: String? = null

    //系统版本
    var sys_version: String? = null

    //物理尺寸 540 * 960
    var screenResolution: String? = null

    //品牌
    var manufacturerName: String? = null

    //新增
    //屏幕分辨率-水平像素
    var screenxpx: String? = null

    //屏幕分辨率-垂直像素
    var screenypx: String? = null

    //是否检测到xposed框架 "true" ,"false"
    var is_xposed_or_root: String? = null

    //获取CPU可运行最大频率
    var cpu_frequency: String? = null

    //手机物理尺寸
    var physical_size: String? = null

    //CPU型号
    var cpu_model: String? = null

    /**
     * 连接到设备的键盘类型：  0-未定义的键盘，  1-无键键盘，  2-标 准外接键盘，  3-12键小键盘(2023/08/24新增)
     * * *  */
    var keyboard: String? = null

    /**
     * 安卓暂存 google advertising id(google广告id）（Y）
     */
    var device_no: String? = null

}