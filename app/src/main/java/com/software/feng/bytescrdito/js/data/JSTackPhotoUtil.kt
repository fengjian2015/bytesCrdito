package com.software.feng.bytescrdito.js.data

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.webkit.WebView
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.software.feng.bytescrdito.common.Cons
import com.software.feng.bytescrdito.js.JSCallBack
import com.software.feng.bytescrdito.util.ActivityManager
import com.software.feng.bytescrdito.util.RiskUtil

/**
 * Time：2024/5/19
 * Author：feng
 * Description：
 */
object JSTackPhotoUtil {
    var mId : String ? = null
    fun tackPhoto(id: String, webView: WebView) {
        mId = id
        XXPermissions.with(ActivityManager.getCurrentActivity()!!)
            .permission(Permission.CAMERA)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    if (allGranted){
                        onGranted(id,webView)
                    }else{
                        JSCallBack.callbackJsErrorPermissions(webView,id, Cons.InvokeCreditoTackPhotoByFront)
                    }
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    JSCallBack.callbackJsErrorPermissions(webView,id, Cons.InvokeCreditoTackPhotoByFront)
                }
            })
    }

    fun onGranted(id: String, webView: WebView){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // 指定使用前置摄像头
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1)
        intent.putExtra("android.intent.extras.CAMERA_FACING_FRONT", 1)
        // 启动相机应用
        ActivityManager.getCurrentActivity()?.startActivityForResult(intent, Cons.TACK_PHOTO)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, webView: WebView) {
        if (requestCode == Cons.TACK_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && mId!=null) {
                    RiskUtil.tackPhoto(data,webView, mId!!,Cons.InvokeCreditoTackPhotoByFront)
                }
            } else {
                mId?.let {
                    JSCallBack.callBackJsError(Cons.InvokeCreditoTackPhotoByFront,webView,
                        it,
                        "Ninguno.")
                }
            }
        }
    }

}