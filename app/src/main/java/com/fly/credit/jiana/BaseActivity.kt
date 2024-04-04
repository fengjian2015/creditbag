package com.fly.credit.jiana

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.ImmersionBar


abstract class BaseActivity<VB : ViewBinding>(
    private val inflate: (LayoutInflater) -> VB
) : AppCompatActivity() {

    lateinit var binding: VB
    var immersionBar: ImmersionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
        initBar()
        initView()
    }

    abstract fun initView()

    private fun initBar() {
        immersionBar = ImmersionBar
            .with(this)
            .fitsSystemWindows(true)
            .statusBarColor(R.color.common_title_bg)
            .statusBarDarkFont(true)
            .keyboardEnable(true)
        immersionBar?.let {
            it.init()
        }
    }


    open fun startActivity(clz: Class<*>?) {
        startActivity(Intent(this@BaseActivity, clz))
    }

}
