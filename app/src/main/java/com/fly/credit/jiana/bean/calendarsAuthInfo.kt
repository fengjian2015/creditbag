package com.fly.credit.jiana.bean

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
class calendarsAuthInfo: java.io.Serializable {
    //bigint	抓取时间 13位时间戳
    var create_time: Long? = 0

    var list: List<calendarsInfo>? = null
    override fun toString(): String {
        return "calendarsAuthInfo(create_time=$create_time, list=$list)"
    }

}