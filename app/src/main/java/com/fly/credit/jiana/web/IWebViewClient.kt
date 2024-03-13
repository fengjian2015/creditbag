package com.fly.credit.jiana.web

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import com.fly.credit.jiana.IWebView
import com.fly.credit.jiana.util.LogUtil

class IWebViewClient constructor(iWebView: IWebView) : WebViewClient() {
    private var isLoadSuccess = true
    private var iWebView: IWebView? = null
    private var context: Context? = null
    private var progressBar: ProgressBar? = null
    private var mUrl: String? = null

    init {
        this.iWebView = iWebView;
        this.context = iWebView.context
        progressBar = iWebView.getProgressbar()
    }

    override fun onPageStarted(view: WebView?, url: String, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        LogUtil.d("onPageStarted->$url")
        mUrl = url
        this.progressBar?.visibility = View.VISIBLE
        this.progressBar?.progress = 0
        isLoadSuccess = true
        iWebView?.showNormalView()
    }


    @Deprecated("Deprecated in Java")
    override fun shouldOverrideUrlLoading(webView: WebView, s: String): Boolean {
        LogUtil.d("重定向:$s")
        if (!TextUtils.isEmpty(s) && s.startsWith("tel:")) {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse(s))
            webView.context.startActivity(intent)
            return true
        }
        if (TextUtils.isEmpty(s)) return false
        if (s.contains("javascript: void(0)") || s.contains("javascript:void(0)")) return false
        progressBar?.visibility = View.VISIBLE
        progressBar?.progress = 0
        val uri = Uri.parse(s)
        //try{} => catch{} added by Barry
        //try{} => catch{} added by Barry
        val schem: String?
        schem = try {
            uri.scheme
        } catch (e: UnsupportedOperationException) {
            return false
        }
        LogUtil.d("schem:$schem")
        if (schem != null && schem.contains("file")) return false
        if (schem != null && (schem.contains("http") || schem.contains("https"))) {
            return false
        } else {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(s)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                webView.context.startActivity(intent)
            } catch (e: Exception) {
                LogUtil.d(e.toString())
            }
            return true
        }
        return false
    }

    override fun onPageFinished(view: WebView?, url: String) {
        super.onPageFinished(view, url)
        LogUtil.d("onPageFinished->$url")
        LogUtil.d("onPageFinished->isLoadSuccess->$isLoadSuccess")
        if (view != null) {
            view.title?.let { iWebView?.getOnTitleListener()?.webTitle(it) }
        }
        progressBar?.visibility = View.GONE
        if (!isLoadSuccess) {
            iWebView?.showFailView()
        } else {
            iWebView?.showNormalView()
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onReceivedError(
        webView: WebView?,
        errorCode: Int,
        description: String,
        failingUrl: String
    ) {
        super.onReceivedError(webView, errorCode, description, failingUrl)
        LogUtil.d("页面加载错误：111:::$description::::$failingUrl")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return
        }
        iWebView?.showFailView()
        isLoadSuccess = false
    }

    override fun onReceivedError(
        webView: WebView?,
        webResourceRequest: WebResourceRequest,
        webResourceError: WebResourceError
    ) {
        super.onReceivedError(webView, webResourceRequest, webResourceError)
        LogUtil.d("页面加载错误：222:::"+ webResourceError.description + ":::" + webResourceRequest.url)
        if (webResourceRequest.isForMainFrame) {
            iWebView?.showFailView()
            isLoadSuccess = false
        }
    }

    override fun onReceivedHttpError(
        webView: WebView?,
        webResourceRequest: WebResourceRequest?,
        webResourceResponse: WebResourceResponse
    ) {
        super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse)
        try {
            if (webResourceRequest != null && webResourceRequest.isForMainFrame && mUrl == webResourceRequest.url.toString() && webResourceResponse.statusCode != 403) {
                LogUtil.d("页面加载错误：333:::" + webResourceResponse.statusCode + ",信息：" + webResourceResponse.reasonPhrase + (mUrl == webResourceRequest.url.toString()))
                iWebView?.showFailView()
                isLoadSuccess = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}