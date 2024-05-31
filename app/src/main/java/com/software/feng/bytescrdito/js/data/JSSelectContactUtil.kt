package com.software.feng.bytescrdito.js.data

import android.app.Activity
import android.content.Intent
import android.webkit.WebView
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.software.feng.bytescrdito.MyApplication
import com.software.feng.bytescrdito.common.Cons
import com.software.feng.bytescrdito.js.JSCallBack
import com.software.feng.bytescrdito.util.ActivityManager
import com.software.feng.bytescrdito.util.RiskUtil

/**
 * Time：2024/5/19
 * Author：feng
 * Description：
 */
object JSSelectContactUtil {
    var mId : String ? = null
    fun selectContact(id: String, webView: WebView) {
        mId = id

        XXPermissions.with(ActivityManager.getCurrentActivity()!!)
            .permission(Permission.READ_PHONE_STATE)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    if (allGranted){
                        onGranted(id,webView)
                    }else{
                        JSCallBack.callbackJsErrorPermissions(webView,id, Cons.InvokeCreditoSelectContact)
                    }
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    JSCallBack.callbackJsErrorPermissions(webView,id, Cons.InvokeCreditoSelectContact)
                }
            })
    }

    fun onGranted(id: String, webView: WebView){
        val intent = Intent()
        intent.action = "android.intent.action.PICK"
        intent.addCategory("android.intent.category.DEFAULT")
        intent.type = "vnd.android.cursor.dir/phone_v2"
        ActivityManager.getCurrentActivity()?.startActivityForResult(intent, Cons.SELECT_CONTACTS_CONTRACT)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, webView: WebView) {
        if (requestCode == Cons.SELECT_CONTACTS_CONTRACT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && mId!=null) {
                    RiskUtil.selectContact(data,webView, mId!!)
                }
            }
        }
    }
}