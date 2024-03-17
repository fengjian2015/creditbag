package com.fly.credit.jiana.manage

import com.fly.credit.jiana.bean.PublicDataResponse
import com.fly.credit.jiana.bean.UserInfoBean
import com.fly.credit.jiana.network.NetCallback
import com.fly.credit.jiana.network.NetClient
import com.fly.credit.jiana.network.NetErrorModel
import com.fly.credit.jiana.network.NetUtil
import com.fly.credit.jiana.util.Cons.KEY_USER_INFO
import com.fly.credit.jiana.util.MMKVCacheUtil
import com.google.gson.Gson

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
object UserInfoManage {
    fun saveUserInfo(user: UserInfoBean) {
        user.let {
            var u = Gson().toJson(user)
            MMKVCacheUtil.putString(KEY_USER_INFO, u)
        }
    }

    fun getUserInfo(): UserInfoBean? {
        var u = MMKVCacheUtil.getString(KEY_USER_INFO)
        try {
            return Gson().fromJson(u, UserInfoBean::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getHomeUrl():String?{
        getUserInfo()?.let {
            return it.homeUrl
        }
        return ""
    }

    fun getToken():String?{
        getUserInfo()?.let {
            return it.token
        }
        return ""
    }

    fun getUserPhone():String?{
        getUserInfo()?.let {
            return it.phone
        }
        return ""
    }


    fun getUserInfoJson(): String {
        return MMKVCacheUtil.getString(KEY_USER_INFO)
    }

    fun logout() {
        val map: MutableMap<String, String> = HashMap()
        NetClient.getNewService().logout(map)
            .compose(NetUtil.applySchedulers())
            .subscribe(object : NetCallback<PublicDataResponse?>() {
                override fun businessFail(netErrorModel: NetErrorModel) {}
                override fun businessSuccess(data: PublicDataResponse?) {
                }
            })
        MMKVCacheUtil.remove(KEY_USER_INFO)
    }
}