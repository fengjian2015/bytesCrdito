package com.software.feng.bytescrdito.js.data

import android.content.Intent
import android.webkit.WebView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.software.feng.bytescrdito.activity.WebActivity
import com.software.feng.bytescrdito.activity.model.WebOpenModel
import com.software.feng.bytescrdito.activity.vm.VMWeb
import com.software.feng.bytescrdito.common.Cons
import com.software.feng.bytescrdito.js.JSCallBack
import com.software.feng.bytescrdito.js.model.JSDataModel
import com.software.feng.bytescrdito.util.ActivityManager

/**
 * Time：2024/5/19
 * Author：feng
 * Description：
 */
object JSForwardOutsideUtil {
    fun forwardOutside(id: String, webView: WebView, data: JSDataModel.JSDataInfo?) {
        try {
            val intent = Intent(ActivityManager.getCurrentActivity(), WebActivity::class.java)
            if (data != null) {
                intent.putExtra("open",WebOpenModel(false,data.value))
            }
            ActivityManager.getCurrentActivity()?.startActivity(intent)

            JSCallBack.callBackJsSuccess(Cons.InvokeCreditoForwardOutside,webView,id)
        } catch (e: Exception) {
            e.printStackTrace()
            JSCallBack.callBackJsError(Cons.InvokeCreditoForwardOutside,webView,id,e.toString())
        }
    }
}