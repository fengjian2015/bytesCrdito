package com.software.feng.bytescrdito.weight

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.software.feng.bytescrdito.util.ActivityManager
import com.software.feng.utillibrary.util.LogUtil

class IWebViewClient constructor(progressBar: ProgressBar, textView: TextView) : WebViewClient() {
    private var isLoadSuccess = true
    private var textView :TextView?= null
    private var progressBar: ProgressBar? = null
    private var mUrl: String? = null

    init {
        this.progressBar = progressBar
        this.textView = textView
    }

    override fun onPageStarted(view: WebView?, url: String, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        LogUtil.d("onPageStarted == $url")
        mUrl = url
        this.progressBar?.visibility = View.VISIBLE
        this.progressBar?.progress = 0
        isLoadSuccess = true
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler, error: SslError?) {
        val mHandler: SslErrorHandler
        mHandler = handler
        val builder = AlertDialog.Builder(ActivityManager.getCurrentActivity())
        builder.setMessage("SSL certificate validation failed")
        builder.setPositiveButton("Go on") { dialog, which -> mHandler.proceed() }
        builder.setNegativeButton("Cancel") { dialog, which -> mHandler.cancel() }
        builder.setOnKeyListener { dialog: DialogInterface, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                mHandler.cancel()
                dialog.dismiss()
                return@setOnKeyListener true
            }
            false
        }
        val dialog = builder.create()
        dialog.show()
    }


    @Deprecated("Deprecated in Java")
    override fun shouldOverrideUrlLoading(webView: WebView, s: String): Boolean {
        if (!TextUtils.isEmpty(s) && s.startsWith("tel:")) {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse(s))
            webView.context.startActivity(intent)
            return true
        }
        return false
    }

    override fun onPageFinished(view: WebView?, url: String) {
        super.onPageFinished(view, url)
        LogUtil.d("onPageFinished == $url")
        progressBar?.visibility = View.GONE
        if (!TextUtils.isEmpty(view!!.title)) {
            textView?.post(Runnable { textView?.text = view.title })
            return
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
        LogUtil.d("加载错误啦：111:::$description::::$failingUrl")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return
        }
        isLoadSuccess = false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceivedError(
        webView: WebView?,
        webResourceRequest: WebResourceRequest,
        webResourceError: WebResourceError
    ) {
        super.onReceivedError(webView, webResourceRequest, webResourceError)
        LogUtil.d("加载错误啦：222:::"+ webResourceError.description + ":::" + webResourceRequest.url)
        if (webResourceRequest.isForMainFrame) {
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
                LogUtil.d("加载错误啦：333:::" + webResourceResponse.statusCode + ",信息：" + webResourceResponse.reasonPhrase + (mUrl == webResourceRequest.url.toString()))
                isLoadSuccess = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}