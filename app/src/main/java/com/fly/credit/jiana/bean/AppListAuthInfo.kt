package com.fly.credit.jiana.bean

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
class AppListAuthInfo :java.io.Serializable{
     var create_time : Long = 0// app最后使用时间/更新时间
    var list: List<AppListInfo>? = null
    override fun toString(): String {
        return "AppListAuthInfo(create_time=$create_time, list=$list)"
    }
}