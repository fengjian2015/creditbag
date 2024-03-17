package com.fly.credit.jiana.bean

/**
 * Time：2024/3/17
 * Author：feng
 * Description：
 */
class LoginlInfoListBase : BaseResponseBean() {
    var data: LoginlInfoList? = null
    override fun toString(): String {
        return "LoginlInfoListBase(data=$data)"
    }
}