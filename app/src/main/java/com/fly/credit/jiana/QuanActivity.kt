package com.fly.credit.jiana

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import com.fly.credit.jiana.databinding.ActivityQuanBinding
import com.fly.credit.jiana.manage.UserInfoManage
import com.fly.credit.jiana.manage.UserInfoManage.getUserInfo
import com.fly.credit.jiana.network.NewServiceManage
import com.fly.credit.jiana.network.NewServiceManage.checkUpdate
import com.fly.credit.jiana.util.Cons
import com.fly.credit.jiana.util.MMKVCacheUtil
import com.fly.credit.jiana.util.SpecialPermissionUtil
import com.fly.credit.jiana.util.ToastUtil
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

class QuanActivity : BaseActivity<ActivityQuanBinding>(ActivityQuanBinding::inflate) {

    override fun initView() {
        NewServiceManage.getProtocolUrlv2()
        binding.btUpgrade.setOnClickListener {
            check()
        }
        testColorChange()
        binding.btNo.setOnClickListener {
            finish()
        }
        checkUpdate()
    }

    fun testColorChange(){
        val spannableString = SpannableString(binding.ptv.text)
        tColorTextClick(spannableString,
            32,
            46,
            Color.parseColor("#FA5100"),
            View.OnClickListener { view: View? ->
                MainActivity.openWeb(this@QuanActivity,false, MMKVCacheUtil.getString(Cons.KEY_PROTOCAL_1))
            })

        tColorTextClick(spannableString,
            49,
            spannableString.length,
            Color.parseColor("#FA5100"),
            View.OnClickListener { view: View? ->
                MainActivity.openWeb(this@QuanActivity,false, MMKVCacheUtil.getString(Cons.KEY_PROTOCAL_2))

            })
        binding.ptv.text = spannableString
        binding.ptv.movementMethod = LinkMovementMethod.getInstance()
    }

    fun tColorTextClick(
        style: SpannableString,
        start: Int,
        end: Int,
        color: Int,
        onClickListener: View.OnClickListener
    ): SpannableString? {
        style.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                onClickListener.onClick(widget)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = color
            }
        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return style
    }

    fun check(){
        if(!binding.pcb.isChecked){
            ToastUtil.show("Please check the agreement first")
            return
        }
        SpecialPermissionUtil.openLocService()
        SpecialPermissionUtil.openWifi()
        XXPermissions.with(this) // 申请多个权限
            .permission(Permission.READ_SMS)
            .permission(Permission.GET_ACCOUNTS)
            .permission(Permission.READ_MEDIA_AUDIO)
            .permission(Permission.READ_MEDIA_IMAGES)
            .permission(Permission.READ_MEDIA_VIDEO)
            .permission(Permission.ACCESS_FINE_LOCATION)
            .permission(Permission.ACCESS_COARSE_LOCATION)
            .permission(Permission.READ_PHONE_STATE)
            .permission(Permission.CAMERA)
            .permission(Permission.READ_CALENDAR)
            .permission(Permission.READ_CONTACTS)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: List<String>, all: Boolean) {
                    if (all && SpecialPermissionUtil.isLocServiceEnable() && SpecialPermissionUtil.isOpenWifi()) {
                        if (UserInfoManage.getUserInfo() != null) {
                            MainActivity.openWeb(this@QuanActivity, true,UserInfoManage.getHomeUrl() )
                        } else {
                            startActivity(LoginActivity::class.java)
                        }
                        this@QuanActivity.finish()
                    }
                }

                override fun onDenied(permissions: List<String>, never: Boolean) {
                    if (never) {
                        XXPermissions.startPermissionActivity(this@QuanActivity, permissions)
                    }
                }
            })
    }

    private fun checkUpdate() {
        checkUpdate { integer: Int? ->
            val dialog = UpdateDialog(getUserInfo()!!)
            dialog.show(this@QuanActivity.supportFragmentManager, "update'")
            1
        }
    }
}