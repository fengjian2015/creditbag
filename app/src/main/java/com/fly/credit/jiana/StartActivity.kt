package com.fly.credit.jiana

import android.content.Intent
import com.fly.credit.jiana.databinding.ActivityStartBinding
import com.fly.credit.jiana.manage.UserInfoManage
import com.fly.credit.jiana.network.NewServiceManage.getProtocolUrlv2
import com.fly.credit.jiana.util.SpecialPermissionUtil
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep

class StartActivity : BaseActivity<ActivityStartBinding>(ActivityStartBinding::inflate) {

    override fun initView() {
        getProtocolUrlv2()
        GlobalScope.launch(Dispatchers.IO){
            sleep(1000)
            withContext(Dispatchers.Main){
                check()
            }
        }
    }
    fun check(){
        if (XXPermissions.isGranted(
                this,
                Permission.ACCESS_COARSE_LOCATION,
                Permission.CAMERA,
                Permission.READ_PHONE_STATE,
                Permission.READ_SMS,
                Permission.GET_ACCOUNTS,
                Permission.READ_CALENDAR
            )
        ) {
            checkWifi()
        } else {
            quan()
        }
    }

    private fun checkWifi() {
        if (!SpecialPermissionUtil.isLocServiceEnable() || !SpecialPermissionUtil.isOpenWifi()) {
            quan()
            return
        }
        if (UserInfoManage.getToken() == null || "" == UserInfoManage.getToken()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            MainActivity.openWeb(this, true, UserInfoManage.getHomeUrl())
            finish()
        }
    }

    private fun quan() {
        startActivity(QuanActivity::class.java)
        finish()
    }
}