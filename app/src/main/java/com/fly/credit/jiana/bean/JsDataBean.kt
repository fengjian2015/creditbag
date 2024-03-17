package com.fly.credit.jiana.bean

import com.google.gson.internal.LinkedTreeMap

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
class JsDataBean ():java.io.Serializable {
    var action: String = ""
    var id: String = ""
    var status: String ="0"//0success // 999 error
    var errorMsg : String =""
    var data: LinkedTreeMap<String, String>? = null
}