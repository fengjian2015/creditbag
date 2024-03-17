package com.fly.credit.jiana.bean

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
class PublicDataResponse : BaseResponseBean(),java.io.Serializable{
    var data: String? = null
    override fun toString(): String {
        return "PublicDataResponse(code=$code, message=$message, data=$data)"
    }
}