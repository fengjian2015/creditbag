package com.fly.credit.jiana.bean

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
class JsCallBackBean(
    var id: String,var action: String,
    var data: String? = null,
    var status: String = "0", errorMsg: String? = null

) : java.io.Serializable {
}