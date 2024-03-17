package com.fly.credit.jiana.bean

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
class AlbumAuthInfo : java.io.Serializable {
    var create_time : Long = 0// app最后使用时间/更新时间
    var list: List<AlbumInfo>? = null
}