package com.fly.credit.jiana.web


import android.view.View

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.ProgressBar


import java.io.File

class IWebChromeClient constructor(progressBar: ProgressBar) : WebChromeClient() {
    private var lineProgressbar: ProgressBar? = null
    init {
        lineProgressbar = progressBar
    }

    override fun onProgressChanged(webView: WebView?, newProgress: Int) {
        super.onProgressChanged(webView, newProgress)
        lineProgressbar?.visibility = View.VISIBLE
        lineProgressbar?.progress = newProgress
        if (newProgress >= 80) {
            lineProgressbar?.visibility = View.GONE
        }
    }

}
