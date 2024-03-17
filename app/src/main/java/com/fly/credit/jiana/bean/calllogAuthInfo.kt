package com.fly.credit.jiana.bean

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
class calllogAuthInfo: java.io.Serializable {
    //抓取时间 13位时间戳
    var create_time: Long? = 0
    var list: List<calllogInfo>? = null
    override fun toString(): String {
        return "calllogAuthInfo(create_time=$create_time, list=$list)"
    }

}