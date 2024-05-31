package com.software.feng.bytescrdito.js.data

import android.webkit.WebView
import com.google.gson.Gson
import com.software.feng.bytescrdito.common.Cons
import com.software.feng.bytescrdito.js.JSCallBack
import com.software.feng.bytescrdito.js.model.JSDataModel
import com.software.feng.bytescrdito.util.DateUtil

/**
 * Time：2024/5/19
 * Author：feng
 * Description：
 */
object JSAppServiceTimeUtil {
    fun appServiceTime(id: String, webView: WebView, data: JSDataModel.JSDataInfo?) {
        try {
            if (data != null) {
                DateUtil.initTime(data.value)
            }
            JSCallBack.callBackJsSuccess(Cons.InvokeCreditoAppServiceTime,webView,id)
        } catch (e: Exception) {
            e.printStackTrace()
            JSCallBack.callBackJsError(Cons.InvokeCreditoAppServiceTime,webView,id,e.toString())

        }
    }
}