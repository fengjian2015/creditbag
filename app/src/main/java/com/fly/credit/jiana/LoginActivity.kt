package com.fly.credit.jiana

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.fly.credit.jiana.databinding.ActivityLoginBinding
import com.fly.credit.jiana.manage.UserInfoManage
import com.fly.credit.jiana.network.NewServiceManage
import com.fly.credit.jiana.util.*

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {
    //秒
    private val codeTime: Long = 60
    private var smsSendTime: Long = 0

    private val handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        @SuppressLint("HandlerLeak")
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (binding.sendTv != null) {
                val currentTime: Long = DateTool.getServerTimestamp() / 1000
                val l = smsSendTime - currentTime
                binding.sendTv.text = "" + l
                if (l <= 0) {
                    smsTimeOver()
                    return
                }
                sendEmptyMessage(1)
            }
        }
    }

    override fun initView() {
        binding.sendBt.setOnClickListener {
            checkCode()
        }

        binding.sendTv.setOnClickListener {
            checkCode()
        }

        binding.login.setOnClickListener {
            checkLogin()
        }
        checkUpdate()
        textColorChange()
    }

    fun textColorChange(){
        val spannableString = SpannableString(binding.ptv.text)
        tColorTextClick(spannableString,
            26,
            40,
            Color.parseColor("#FA5100"),
            View.OnClickListener { view: View? ->
                MainActivity.openWeb(this@LoginActivity,false, MMKVCacheUtil.getString(Cons.KEY_PROTOCAL_2))

            })
        tColorTextClick(spannableString,
            43,
            spannableString.length,
            Color.parseColor("#FA5100"),
            View.OnClickListener { view: View? ->
                MainActivity.openWeb(this@LoginActivity,false, MMKVCacheUtil.getString(Cons.KEY_PROTOCAL_2))

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

    fun checkCode(){
        if (binding.edMobiletv.text.toString().isEmpty()){
            ToastUtil.show("Fill in your phone number")
            return
        }
        NewServiceManage.sendVerifyCode(binding.edMobiletv.text.toString(),object :Function1<Boolean,Boolean>{
            override fun invoke(p1: Boolean): Boolean {
                if (p1){
                    sendSms()
                }else{

                }
                return true
            }
        })
    }

    fun sendSms(){
        binding.sendBt.visibility = View.GONE
        binding.sendBt.isClickable = false
        binding.sendBt.isEnabled = false

        binding.sendTv.visibility = View.VISIBLE
        binding.sendTv.isClickable = false
        binding.sendTv.isEnabled = false
        binding.sendTv.text = "60"
        smsSendTime = DateTool.getServerTimestamp() / 1000 + codeTime
        handler.sendEmptyMessage(1)
    }
    fun smsTimeOver(){
        binding.sendTv.visibility = View.VISIBLE
        binding.sendTv.isClickable = true
        binding.sendTv.isEnabled = true
        binding.sendTv.text = "Resend OTP"
    }

    fun checkLogin(){
        if (binding.edMobiletv.text.toString().isEmpty()){
            ToastUtil.show("Fill in your phone number")
            return
        }
        if (binding.edCodetv.text.toString().isEmpty()){
            ToastUtil.show("Fill in OTP")
        }
        if (!binding.pcb.isChecked){
            ToastUtil.show("Please check the agreement first")
            return
        }
        NewServiceManage.login(binding.edMobiletv.text.toString(),binding.edCodetv.text.toString(),object : Function1<Int,Int>{
            override fun invoke(p1: Int): Int {
                if (p1 == 1){
                    MainActivity.openWeb(this@LoginActivity,true,UserInfoManage.getHomeUrl())
                }else if (p1 == 3){
                    val dialog = UserInfoManage.getUserInfo()?.let { UpdateDialog(it) }
                    val activity =  ActivityManager.getCurrentActivity() as FragmentActivity
                    dialog?.show(activity.supportFragmentManager, "update'")
                }
                return 0
            }
        })
    }

    private fun checkUpdate() {
//        NewServiceManage.checkUpdate {
//            val dialog = UpdateDialog(UserInfoManage.getUserInfo()!!)
//            dialog.show(this@LoginActivity.supportFragmentManager, "update'")
//            1
//        }
    }


}