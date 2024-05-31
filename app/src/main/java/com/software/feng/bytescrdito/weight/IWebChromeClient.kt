package com.software.feng.bytescrdito.weight

import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.ProgressBar

/**
 * Time：2024/5/13
 * Author：feng
 * Description：
 */
class IWebChromeClient(progressBar: ProgressBar) : WebChromeClient() {
    var mProgressBar: ProgressBar ? = null

    init {
        mProgressBar = progressBar
    }

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        mProgressBar?.visibility = View.VISIBLE
        mProgressBar?.progress = newProgress
        if (newProgress > 90){
            mProgressBar?.visibility = View.GONE
        }
    }
}