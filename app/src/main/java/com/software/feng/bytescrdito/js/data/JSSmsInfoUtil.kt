package com.software.feng.bytescrdito.js.data

import android.webkit.WebView
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.software.feng.bytescrdito.MyApplication
import com.software.feng.bytescrdito.common.Cons
import com.software.feng.bytescrdito.http.NetRequestManage
import com.software.feng.bytescrdito.http.model.risk.AuthInfoBean
import com.software.feng.bytescrdito.http.model.risk.AuthSMSListBean
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
object JSSmsInfoUtil {
    fun smsInfo(id: String, webView: WebView) {
        XXPermissions.with(ActivityManager.getCurrentActivity()!!)
            .permission(Permission.READ_PHONE_STATE)
            .permission(Permission.READ_SMS)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    if (allGranted){
                        onGranted(id,webView)
                    }else{
                        JSCallBack.callbackJsErrorPermissions(webView,id, Cons.InvokeCreditoSmsInfo)
                    }
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    JSCallBack.callbackJsErrorPermissions(webView,id, Cons.InvokeCreditoSmsInfo)
                }
            })
    }

    fun onGranted(id: String, webView: WebView){
        GlobalScope.launch(Dispatchers.IO){
            var smsAuthInfo = AuthSMSListBean()
            smsAuthInfo.create_time = DateUtil.getServerTimestamp()/1000
            smsAuthInfo.list = RiskUtil.getSmsInfo()
            withContext(Dispatchers.Main){
                LogUtil.d("通讯录加载完成：$smsAuthInfo")
                var applyInfoBean = AuthInfoBean()
                applyInfoBean.sms = smsAuthInfo
                NetRequestManage.uploadCreditModeLoanWardAuth(applyInfoBean,webView,id,Cons.InvokeCreditoSmsInfo)
            }
        }
    }
}