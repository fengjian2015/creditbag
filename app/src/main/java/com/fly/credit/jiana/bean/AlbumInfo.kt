package com.fly.credit.jiana.bean

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
class AlbumInfo : java.io.Serializable {
    //名称
    var name: String? = null

    //拍摄时间戳
    var take_time: Long? = 0

    //保存时间戳
    var save_time: Long? = 0

    //照片方向
    var orientation: String? = null

    //水平方向分辨率
    var x_resolution: String? = null

    //水平方向分辨率
    var y_resolution: String? = null

    //海拔
    var gps_altitude: String? = null

    //gps定位方式
    var gps_processing_method: String? = null

    //镜头生产商
    var lens_make: String? = null

    //镜头模型
    var lens_model: String? = null

    //镜头焦距
    var focal_length: String? = null

    //闪光灯状态
    var flash: String? = null

    //软件
    var software: String? = null

    //长
    var length: String? = null

    //以下是为了兼容loanward
    //拍摄者（无拍摄者则获取手机品牌）-N
    var author: String? = null

    //照片高度，单位：像素-N
    var height: String? = null

    //照片宽度，单位：像素-N
    var width: String? = null

    //经度-N
    var longitude: String? = null

    //纬度-N
    var latitude: String? = null

    //拍摄照片的手机机型-N
    var model: String? = null

    //照片修改时间(格式：yyyy-mm-dd HH:mm:ss)拿不到，用拍摄时间
    var updateTime: String? = null

}