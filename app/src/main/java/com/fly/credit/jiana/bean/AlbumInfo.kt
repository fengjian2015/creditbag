package com.fly.credit.jiana.bean

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
class AlbumInfo : java.io.Serializable {
    //名称
    var name: String? = ""

    //拍摄时间戳
    var take_time: Long? = 0

    //保存时间戳
    var save_time: Long? = 0

    //照片方向
    var orientation: String? = ""

    //水平方向分辨率
    var x_resolution: String? = ""

    //水平方向分辨率
    var y_resolution: String? = ""

    //海拔
    var gps_altitude: String? = ""

    //gps定位方式
    var gps_processing_method: String? = ""

    //镜头生产商
    var lens_make: String? = ""

    //镜头模型
    var lens_model: String? = ""

    //镜头焦距
    var focal_length: String? = ""

    //闪光灯状态
    var flash: String? = ""

    //软件
    var software: String? = ""

    //长
    var length: String? = ""

    //以下是为了兼容loanward
    //拍摄者（无拍摄者则获取手机品牌）-N
    var author: String? = ""

    //照片高度，单位：像素-N
    var height: String? = ""

    //照片宽度，单位：像素-N
    var width: String? = ""

    //经度-N
    var longitude: String? = ""

    //纬度-N
    var latitude: String? = ""

    //拍摄照片的手机机型-N
    var model: String? = ""

    //照片修改时间(格式：yyyy-mm-dd HH:mm:ss)拿不到，用拍摄时间
    var updateTime: String? = ""

}