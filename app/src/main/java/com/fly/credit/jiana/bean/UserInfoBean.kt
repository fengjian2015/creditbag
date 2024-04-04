package com.fly.credit.jiana.bean

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
class UserInfoBean : java.io.Serializable {
    var homeUrl : String? = ""
    var token : String? = ""
    var name : String? = ""
    var phone : String? = ""
    var isNew : Boolean? = true
    var mustupdate: String? =""
    var appInstallUrl : String? = ""
    override fun toString(): String {
        return "UserInfoBean(homeUrl=$homeUrl, token=$token, name=$name, phone=$phone, isNew=$isNew, mustupdate='$mustupdate', appInstallUrl='$appInstallUrl')"
    }

}