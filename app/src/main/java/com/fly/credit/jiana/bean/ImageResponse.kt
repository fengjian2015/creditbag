package com.fly.credit.jiana.bean

/**
 * Time：2024/3/17
 * Author：feng
 * Description：
 */
class ImageResponse : BaseResponseBean() {
    var data : String? =null
    override fun toString(): String {
        return "ImageResponse(data=$data)"
    }
}