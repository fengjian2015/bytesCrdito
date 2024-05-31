package com.software.feng.bytescrdito.js.data

import android.content.Intent
import android.webkit.WebView
import com.google.gson.Gson
import com.software.feng.bytescrdito.MyApplication
import com.software.feng.bytescrdito.activity.MainActivity
import com.software.feng.bytescrdito.common.CommonUtil
import com.software.feng.bytescrdito.common.Cons
import com.software.feng.bytescrdito.common.Cons.KEY_USER_INFO
import com.software.feng.bytescrdito.http.NetRequestManage
import com.software.feng.bytescrdito.http.model.UserInfoResponse
import com.software.feng.bytescrdito.js.JSCallBack
import com.software.feng.bytescrdito.js.model.JSCallBackUserInfoModel
import com.software.feng.bytescrdito.util.ActivityManager
import com.tencent.mmkv.MMKV

/**
 * Time：2024/5/19
 * Author：feng
 * Description：
 */
object JSUserInfoUtil {
    fun getJSUserInfo(id: String , webView: WebView){
        var jSCallBackModel = JSCallBackUserInfoModel()
        jSCallBackModel.data =MMKV.defaultMMKV().getString(KEY_USER_INFO,"")
        jSCallBackModel.dev = CommonUtil.getVersionName(MyApplication.application)
        var data= Gson().toJson(jSCallBackModel)
        JSCallBack.callBackJsSuccess(Cons.InvokeCreditoUserInfo,webView,id,data)
    }

    fun setUserInfo(userInfoBean: UserInfoResponse){
        userInfoBean?.let {
            val toJson = Gson().toJson(userInfoBean)
            MMKV.defaultMMKV().putString(KEY_USER_INFO,toJson)
        }
    }

    fun getUserInfo(): UserInfoResponse? {
        var u = MMKV.defaultMMKV().getString(KEY_USER_INFO,"")
        try {
            return Gson().fromJson(u, UserInfoResponse::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getPhone(): String?{
        var info = getUserInfo()
        if (info== null){
            return ""
        }else{
            return info.phone
        }
    }

    fun getToken(): String?{
        var info = getUserInfo()
        if (info== null){
            return ""
        }else{
            return info.token
        }
    }

    fun getHomeUrl(): String?{
        var info = getUserInfo()
        if (info== null){
            return ""
        }else{
            return info.homeUrl
        }
    }

    fun logout(){
        NetRequestManage.logout()
        val intent = Intent(ActivityManager.getCurrentActivity(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        ActivityManager.getCurrentActivity()?.startActivity(intent)
        MMKV.defaultMMKV().remove(KEY_USER_INFO)
    }
}