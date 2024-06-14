package com.software.feng.bytescrdito.js.data

import android.webkit.WebView
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.software.feng.bytescrdito.MyApplication
import com.software.feng.bytescrdito.common.Cons
import com.software.feng.bytescrdito.http.NetRequestManage
import com.software.feng.bytescrdito.http.model.risk.AuthCalendarsBean
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
object JSCalendarInfoUtil {
    fun calendarInfo(id: String, webView: WebView) {
        XXPermissions.with(ActivityManager.getCurrentActivity()!!)
            .permission(Permission.READ_CALENDAR)
            .request(object : OnPermissionCallback {

                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    if (allGranted){
                        onGranted(id,webView)
                    }else{
                        JSCallBack.callbackJsErrorPermissions(webView,id, Cons.InvokeCreditoCalendarInfo)
                    }
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    JSCallBack.callbackJsErrorPermissions(webView,id, Cons.InvokeCreditoCalendarInfo)
                }
            })
    }

    fun onGranted(id: String, webView: WebView){
        GlobalScope.launch(Dispatchers.IO){
            val authInfoBean = AuthInfoBean()
            val calendars = AuthCalendarsBean()
            calendars.create_time = DateUtil.getServerTimestamp()
            calendars.list = RiskUtil.getCalendersList()
            authInfoBean.calendars = calendars
            withContext(Dispatchers.Main){
                LogUtil.d("日历信息：$calendars")
                NetRequestManage.uploadCreditModeLoanWardAuth(authInfoBean,webView,id,Cons.InvokeCreditoCalendarInfo)
            }
        }
    }
}