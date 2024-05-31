package com.software.feng.bytescrdito.js.data

import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import com.software.feng.bytescrdito.common.Cons
import com.software.feng.bytescrdito.js.JSCallBack
import com.software.feng.bytescrdito.js.model.JSDataModel
import com.software.feng.bytescrdito.util.ActivityManager

/**
 * Time：2024/5/19
 * Author：feng
 * Description：
 */
object JSOpenBrowserUtil {
    fun openBrowser(id: String, webView: WebView, data: JSDataModel.JSDataInfo?) {
        try {
            var url = data?.value
            if (url != null && !url.startsWith("http") && !url.startsWith("file")) {
                url = "https://$url"
            }
            val uri: Uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            ActivityManager.getCurrentActivity()?.startActivity(intent)
            JSCallBack.callBackJsSuccess(Cons.InvokeCreditoOpenBrowser,webView,id)
        }catch (e: Exception){
            e.printStackTrace()
            JSCallBack.callBackJsError(Cons.InvokeCreditoOpenBrowser,webView,id,e.toString())

        }
    }
}