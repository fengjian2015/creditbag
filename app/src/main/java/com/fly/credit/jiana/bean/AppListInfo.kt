package com.fly.credit.jiana.bean

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
class AppListInfo :java.io.Serializable{
    //app名
    var app_name: String? = null
    //app package_name
    var package_name: String? = null
    //app版本号
    var version_code: String? = null
    //版本名
    var version_name: String? = null
    //app安装时间--ms
    var first_install_time: Long? = 0
    //app更新时间--ms
    var last_update_time: Long? = 0
    //是否系统app 1：安装包，2：系统包
    var is_system: String? = null
    override fun toString(): String {
        return "AppListInfo(app_name=$app_name, package_name=$package_name, version_code=$version_code, version_name=$version_name, first_install_time=$first_install_time, last_update_time=$last_update_time, is_system=$is_system)"
    }

}