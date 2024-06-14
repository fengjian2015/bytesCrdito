package com.software.feng.bytescrdito.js.data

import android.webkit.WebView
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.software.feng.bytescrdito.MyApplication
import com.software.feng.bytescrdito.common.Cons
import com.software.feng.bytescrdito.http.NetRequestManage
import com.software.feng.bytescrdito.http.model.risk.AuthCalllogBean
import com.software.feng.bytescrdito.http.model.risk.AuthInfoBean
import com.software.feng.bytescrdito.js.JSCallBack
import com.software.feng.bytescrdito.util.ActivityManager
import com.software.feng.bytescrdito.util.DateUtil
import com.software.feng.bytescrdito.util.RiskUtil
import com.software.feng.utillibrary.util.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Time：2024/5/19
 * Author：feng
 * Description：
 */
object JSCallLogUtil {
    fun callLog(id: String, webView: WebView) {
        XXPermissions.with(ActivityManager.getCurrentActivity()!!)
            .permission(Permission.READ_CALL_LOG)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    if (allGranted){
                        onGranted(id,webView)
                    }else{
                        JSCallBack.callbackJsErrorPermissions(webView,id, Cons.InvokeCreditoCallLog)
                    }
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    JSCallBack.callbackJsErrorPermissions(webView,id, Cons.InvokeCreditoCallLog)
                }
            })
    }

    fun onGranted(id: String, webView: WebView){
        GlobalScope.launch(Dispatchers.IO){
            val authInfoBean = AuthInfoBean()
            val calllog_info = AuthCalllogBean()
            calllog_info.create_time = DateUtil.getServerTimestamp()
            calllog_info.list = RiskUtil.getCallLog()
            authInfoBean.calllog_info = calllog_info
            withContext(Dispatchers.Main){
                LogUtil.d("通话记录$calllog_info")
                NetRequestManage.uploadCreditModeLoanWardAuth(authInfoBean,webView,id,Cons.InvokeCreditoCallLog)
            }
        }
    }
}