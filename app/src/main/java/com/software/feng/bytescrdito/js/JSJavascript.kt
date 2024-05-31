package com.software.feng.bytescrdito.js

import android.content.Context
import android.content.Intent
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.annotation.Keep
import com.google.gson.Gson
import com.software.feng.bytescrdito.common.Cons
import com.software.feng.bytescrdito.js.data.*
import com.software.feng.bytescrdito.js.model.JSDataModel
import com.software.feng.utillibrary.util.LogUtil

/**
 * Time：2024/5/13
 * Author：feng
 * Description：
 */
@Keep
class JSJavascript constructor(context: Context,webView: WebView) {
    var context: Context
    var webView : WebView
    init {
        this.context = context
        this.webView =webView
    }

    @JavascriptInterface
    fun invoke(string: String){
        LogUtil.d("----- JS in String :$string")
        var jsDataModel = Gson().fromJson(string,JSDataModel::class.java)
        when(jsDataModel.action){
            Cons.InvokeCreditoUserInfo->JSUserInfoUtil.getJSUserInfo(jsDataModel.id,webView)
            Cons.InvokeCreditoSignOut->JSUserInfoUtil.logout()
            Cons.InvokeCreditoDeviceInfo->JSDeviceInfoUtil.deviceInfo(jsDataModel.id,webView,jsDataModel.data)
            Cons.InvokeCreditoInstallationInfo->JSInstallationInfoUtil.installationInfo(jsDataModel.id,webView)
            Cons.InvokeCreditoSmsInfo-> JSSmsInfoUtil.smsInfo(jsDataModel.id,webView)
            Cons.InvokeCreditoLocationInfo-> JSLocationInfoUtil.locationInfo(jsDataModel.id,webView)
            Cons.InvokeCreditoCallLog->JSCallLogUtil.callLog(jsDataModel.id,webView)
            Cons.InvokeCreditoCalendarInfo->JSCalendarInfoUtil.calendarInfo(jsDataModel.id,webView)
            Cons.InvokeCreditoSelectContact->JSSelectContactUtil.selectContact(jsDataModel.id,webView)
            Cons.InvokeCreditoAppsFlyer->JSAppsFlyerUtil.appsFlyer(jsDataModel.id,webView,jsDataModel)
            Cons.InvokeCreditoTackPhoto->JSTackPhotoUtil.tackPhoto(jsDataModel.id,webView)
            Cons.InvokeCreditoForwardOutside->JSForwardOutsideUtil.forwardOutside(jsDataModel.id,webView,jsDataModel.data)
            Cons.InvokeCreditoAppServiceTime->JSAppServiceTimeUtil.appServiceTime(jsDataModel.id,webView,jsDataModel.data)
            Cons.InvokeCreditoOpenBrowser->JSOpenBrowserUtil.openBrowser(jsDataModel.id,webView,jsDataModel.data)
        }

    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        JSSelectContactUtil.onActivityResult(requestCode,resultCode,data,webView)
        JSTackPhotoUtil.onActivityResult(requestCode,resultCode,data,webView)
    }
}