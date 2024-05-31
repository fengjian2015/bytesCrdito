package com.software.feng.bytescrdito.js

import android.webkit.ValueCallback
import android.webkit.WebView
import com.google.gson.Gson
import com.software.feng.bytescrdito.js.model.JSCallbackModel
import com.software.feng.utillibrary.util.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.IDN

/**
 * Time：2024/5/19
 * Author：feng
 * Description：
 */
object JSCallBack {

    fun callBackJsSuccess(action:  String,webView: WebView,id: String){
        callBackJs(webView, JSCallbackModel(action,id,"0",""),null)
    }

    fun callBackJsSuccess(action:  String,webView: WebView,id: String,data:String){
        callBackJs(webView, JSCallbackModel(action,id,"0",data),null)
    }

    fun callBackJsError(action:  String,webView: WebView,id: String,error:String){
        callBackJs(webView, JSCallbackModel(action,id,"999","",error),null)
    }

    fun callBackJsError(action:  String,webView: WebView,id: String){
        callBackJs(webView, JSCallbackModel(action,id,"999","",""),null)
    }
    fun callbackJsErrorPermissions(webView: WebView, id: String, event: String) {
        callBackJs(webView, JSCallbackModel(event, id, "999","","Authorize permissions before submitting application"),null)

    }

    private fun callBackJs(webView: WebView, model: JSCallbackModel?, callback: ValueCallback<*>?) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val toJson = Gson().toJson(model)
                LogUtil.d("回调数据结果：$toJson")
                val js = "javascript: window.JSForCreditoCallback && window.JSForCreditoCallback($toJson);"
                webView.evaluateJavascript(js, callback as ValueCallback<String>?)
            } catch (w: Throwable) {
            }
        }
    }
}