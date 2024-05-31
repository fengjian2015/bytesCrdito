package com.software.feng.bytescrdito.weight

import android.os.Build
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import com.software.feng.bytescrdito.BuildConfig

/**
 * Time：2024/5/13
 * Author：feng
 * Description：
 */
object WebSetting {
    fun initWeb(webView: WebView){
        try {
            val webSettings = webView.settings
            webSettings.useWideViewPort = true
            webSettings.allowUniversalAccessFromFileURLs = true
            webSettings.blockNetworkImage = false
            webSettings.fantasyFontFamily = "fantasy"
            webSettings.loadWithOverviewMode = true
            webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
            webSettings.allowContentAccess = true
            webSettings.allowFileAccess = true
            webSettings.allowFileAccessFromFileURLs = true
            webSettings.fixedFontFamily = "monospace"
            webSettings.textZoom = 100
            webSettings.javaScriptCanOpenWindowsAutomatically = false
            webSettings.javaScriptEnabled = true
            webSettings.lightTouchEnabled = false
            webSettings.loadWithOverviewMode = true
            webSettings.displayZoomControls = true
            webSettings.databaseEnabled = true
            webSettings.savePassword = false
            webSettings.domStorageEnabled = true
            webSettings.blockNetworkLoads = false
            webSettings.builtInZoomControls = false
            webSettings.cursiveFontFamily = "cursive"
            webSettings.loadsImagesAutomatically = true
            webSettings.savePassword = false
            webSettings.serifFontFamily = "serif"
            webSettings.standardFontFamily = "sans-serif"
            webSettings.setSupportMultipleWindows(false)
            webSettings.mediaPlaybackRequiresUserGesture = true
            webSettings.sansSerifFontFamily = "sans-serif"
            webSettings.saveFormData = true
            webSettings.setSupportZoom(true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
                }
            } catch (e: Throwable) {
            }
            webSettings.setEnableSmoothTransition(false)
            webSettings.setGeolocationEnabled(true)
            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
            if (Build.VERSION.SDK_INT >= 21) {
                cookieManager.setAcceptThirdPartyCookies(webView, true)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}