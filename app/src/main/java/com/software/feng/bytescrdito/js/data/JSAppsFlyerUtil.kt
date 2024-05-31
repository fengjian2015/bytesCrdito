package com.software.feng.bytescrdito.js.data

import android.webkit.WebView
import com.software.feng.bytescrdito.http.NetRequestManage
import com.software.feng.bytescrdito.js.model.JSDataModel
import com.software.feng.bytescrdito.util.AppsFlyerUtil
import com.software.feng.utillibrary.util.LogUtil

/**
 * Time：2024/5/19
 * Author：feng
 * Description：
 */
object JSAppsFlyerUtil {
    fun appsFlyer(id: String, webView: WebView, jsDataModel: JSDataModel) {
        jsDataModel.data?.let {
            LogUtil.d("接口埋点事件：${it.eventName}")
            AppsFlyerUtil.postAF(it.eventName)
        }
    }
}