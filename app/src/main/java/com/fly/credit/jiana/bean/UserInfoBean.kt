package com.fly.credit.jiana.bean

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
class UserInfoBean : java.io.Serializable {
    var homeUrl : String? = null
    var token : String? = null
    var name : String? = null
    var phone : String? = null
    var isNew : Boolean? = true
    var mustupdate: String? =null
    var appInstallUrl : String? = null
    override fun toString(): String {
        return "UserInfoBean(homeUrl=$homeUrl, token=$token, name=$name, phone=$phone, isNew=$isNew, mustupdate='$mustupdate', appInstallUrl='$appInstallUrl')"
    }

}