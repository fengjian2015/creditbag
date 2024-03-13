package com.fly.credit.jiana

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.fly.credit.jiana.web.IWebChromeClient
import com.fly.credit.jiana.web.IWebSetting
import com.fly.credit.jiana.web.IWebViewClient
import com.fly.credit.jiana.util.LogUtil

class IWebView : RelativeLayout {
    private var fallView: View? = null
    private var onTitleListener: OnTitleListener? = null
    private lateinit var mLoading: ProgressBar
    private lateinit var mFullFrameLayout: FrameLayout
    private lateinit var mWebView: WebView
    private var mLayoutFail: LinearLayout? = null
    private lateinit var mIWebChromeClient: IWebChromeClient

    constructor(mContext: Context) : super(mContext) {
        init(mContext)
    }

    constructor(mContext: Context, mAttributeSet: AttributeSet) : super(mContext, mAttributeSet) {
        init(mContext)
    }

    constructor(mContext: Context, mAttributeSet: AttributeSet, i: Int) : super(
        mContext,
        mAttributeSet,
        i
    ) {
        init(mContext)
    }

    private fun init(context: Context) {
        var view: View = LayoutInflater.from(context).inflate(R.layout.layout_webview, this, true)
        mLoading = view.findViewById<ProgressBar>(R.id.wb_loading)
        mFullFrameLayout = view.findViewById<FrameLayout>(R.id.wb_fullFrameLayout)
        mWebView = view.findViewById<WebView>(R.id.wb_webView)
        mLayoutFail = view.findViewById<LinearLayout>(R.id.wb_layoutFail)

        fallView =
            LayoutInflater.from(context).inflate(R.layout.layout_common_failview, null, false)
        mWebView?.let {
            IWebSetting.webViewInit(it)
            mIWebChromeClient = IWebChromeClient(this)
            it.webChromeClient = mIWebChromeClient
            it.webViewClient = IWebViewClient(this)
        }
    }
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mIWebChromeClient.onActivityResult(requestCode, resultCode, data)
    }

    fun getWebView(): WebView {
        return mWebView
    }

    fun getProgressbar(): ProgressBar? {
        return mLoading
    }

    fun getFullFrameLayout(): FrameLayout? {
        return mFullFrameLayout
    }

    fun showFailView() {
        LogUtil.d("showFailView")
        if (fallView == null) return
        mLayoutFail?.removeAllViews()
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        mLayoutFail?.addView(fallView, params)
        mLayoutFail?.visibility = VISIBLE
        mWebView?.visibility = GONE
        mLoading?.visibility = GONE
        mFullFrameLayout?.visibility = GONE
    }

    fun showNormalView() {
        LogUtil.d("showNormalView")
        mLayoutFail?.visibility = GONE
        mWebView?.visibility = VISIBLE
        mFullFrameLayout?.visibility = GONE
    }

    fun getOnTitleListener(): OnTitleListener? {
        return onTitleListener
    }

    fun setOnTitleListener(l: OnTitleListener) {
        onTitleListener = l
    }


    private fun resetView(view: View?) {
        if (view != null) {
            val parent = view.parent
            if (parent != null) {
                val group = parent as ViewGroup
                group.removeView(view)
            }
        }
    }

    public interface OnTitleListener {
        fun webTitle(title: String)
    }
}