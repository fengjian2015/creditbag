package com.fly.credit.jiana.bean

import java.io.Serializable

class LoginlInfoList :Serializable{
    var homeUrl :String = ""
    var token :String = ""
    var name :String = ""
    var phone :String = ""
    var isNew :Boolean = true
    var mustUpdate: String =""
    var appInstallUrl : String = ""
    override fun toString(): String {
        return "LoginlInfoList(homeUrl='$homeUrl', token='$token', name='$name', phone='$phone', isNew=$isNew)"
    }
}