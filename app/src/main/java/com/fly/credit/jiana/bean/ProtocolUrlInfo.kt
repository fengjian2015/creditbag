package com.fly.credit.jiana.bean

import java.io.Serializable

class ProtocolUrlInfo :Serializable{
    var protocalName :String =""//协议名称
    // 协议类型 1.隐私协议（登录页） 2.服务条款（登录页）
    var protocalType :Int =0
    var url :String =""//协议地址

}