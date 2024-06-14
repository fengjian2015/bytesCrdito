package com.software.feng.bytescrdito.js.data

import android.webkit.WebView
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.software.feng.bytescrdito.MyApplication
import com.software.feng.bytescrdito.common.Cons
import com.software.feng.bytescrdito.http.NetRequestManage
import com.software.feng.bytescrdito.http.model.risk.AuthAppListBean
import com.software.feng.bytescrdito.http.model.risk.AuthInfoBean
import com.software.feng.bytescrdito.js.JSCallBack
import com.software.feng.bytescrdito.util.ActivityManager
import com.software.feng.bytescrdito.util.DateUtil
import com.software.feng.bytescrdito.util.RiskUtil
import com.software.feng.utillibrary.util.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Time：2024/5/19
 * Author：feng
 * Description：
 */
object JSInstallationInfoUtil {
    fun installationInfo(id: String, webView: WebView) {
        XXPermissions.with(ActivityManager.getCurrentActivity()!!)
            .permission(Permission.READ_PHONE_STATE)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    if (allGranted){
                        onGrantedAll(id,webView)
                    }else{
                        JSCallBack.callbackJsErrorPermissions(webView,id, Cons.InvokeCreditoInstallationInfo)
                    }
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    JSCallBack.callbackJsErrorPermissions(webView,id, Cons.InvokeCreditoInstallationInfo)
                }
            })
    }

    fun onGrantedAll(id: String, webView: WebView) {
        GlobalScope.launch(Dispatchers.IO){
            val authAppListBean = AuthAppListBean()
            authAppListBean.create_time =DateUtil.getServerTimestamp()
            authAppListBean.list = RiskUtil.getInstallationInfos()

            val authInfoBean = AuthInfoBean()
            authInfoBean.applist = authAppListBean
            LogUtil.d("app安装信息：$authAppListBean")
            NetRequestManage.uploadCreditModeLoanWardAuth(authInfoBean,webView,id,Cons.InvokeCreditoInstallationInfo)
        }
    }
}