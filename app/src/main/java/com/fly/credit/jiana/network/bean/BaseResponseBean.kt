package com.fly.credit.jiana.network.bean

import java.io.Serializable

open class BaseResponseBean : Serializable{
    var code: Int = 0
    var message: String = ""
}