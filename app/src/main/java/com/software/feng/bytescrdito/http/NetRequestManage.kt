package com.software.feng.bytescrdito.http

import android.os.Build
import android.util.Base64
import android.webkit.WebView
import android.widget.Toast
import com.appsflyer.AppsFlyerLib
import com.google.gson.Gson
import com.software.feng.bytescrdito.MyApplication
import com.software.feng.bytescrdito.common.Cons
import com.software.feng.bytescrdito.http.model.*
import com.software.feng.bytescrdito.http.model.risk.AuthInfoBean
import com.software.feng.bytescrdito.js.JSCallBack
import com.software.feng.bytescrdito.js.data.JSCallLogUtil
import com.software.feng.bytescrdito.js.data.JSUserInfoUtil
import com.software.feng.bytescrdito.js.model.JSDataModel
import com.software.feng.bytescrdito.util.ActivityManager
import com.software.feng.bytescrdito.util.AppsFlyerUtil
import com.software.feng.bytescrdito.util.DateUtil
import com.software.feng.bytescrdito.util.RiskUtil
import com.software.feng.utillibrary.http.NetCallback
import com.software.feng.utillibrary.http.NetErrorModel
import com.software.feng.utillibrary.http.NetUtil
import com.software.feng.utillibrary.util.LoadingUtil
import com.tencent.mmkv.MMKV
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.File
import java.io.IOException

/**
 * Time：2024/5/20
 * Author：feng
 * Description：
 */
object NetRequestManage {

    fun getSMS(phone: String, function: Function1<Int,Int>){
        val map: MutableMap<String, String> = HashMap()
        map["phone"] = phone
        NetService.getNewService().getSMS(map)
            .compose(NetUtil.applySchedulers())
            .doOnSubscribe { LoadingUtil.show(ActivityManager.getCurrentActivity()) }
            .doFinally { LoadingUtil.dismiss() }
            .subscribe(object : NetCallback<SMSResponse?>() {
                override fun businessFail(netErrorModel: NetErrorModel?) {
                    if (netErrorModel != null) {
                        Toast.makeText(MyApplication.application,netErrorModel.message,Toast.LENGTH_SHORT).show()
                    }
                    function.invoke(1)
                }

                override fun businessSuccess(data: SMSResponse?) {
                    if (data?.code==200){
                        function.invoke(0)
                    }else{
                        function.invoke(1)
                    }
                    if (data != null) {
                        Toast.makeText(MyApplication.application,data.message,Toast.LENGTH_SHORT).show()
                    }
                }

            })
    }

    fun Login(phone: String,code: String, function: Function1<Int,Int>){
        val map: MutableMap<String, String> = HashMap()
        map["Phone"] = phone
        map["Code"] = code
        map["mobilePhoneBrands"] = Build.BRAND
        map["appMarketCode"] = "Google"
        map["deviceModel"] = Build.MODEL
        map["version"] = RiskUtil.getVersionName()
        map["client"] = "android"
        map["clientVersion"] = Build.DISPLAY
        map["extension"] = MMKV.defaultMMKV().getString(Cons.EXTENSION,"").toString()
        map["channelCode"] = MMKV.defaultMMKV().getString(Cons.KEY_AF_CHANNEL,"").toString()
        map["appsFlyerId"] = AppsFlyerLib.getInstance().getAppsFlyerUID(MyApplication.application)?:""
        NetService.getNewService()
            .login(map)
            .compose(NetUtil.applySchedulers())
            .doOnSubscribe { LoadingUtil.show(ActivityManager.getCurrentActivity()) }
            .doFinally { LoadingUtil.dismiss() }
            .subscribe(object : NetCallback<UserInfoResponseBase?>() {
                override fun businessFail(netErrorModel: NetErrorModel?) {
                    if (netErrorModel != null) {
                        Toast.makeText(MyApplication.application,netErrorModel.message,Toast.LENGTH_SHORT).show()
                    }
                    function.invoke(1)
                }

                override fun businessSuccess(data: UserInfoResponseBase?) {
                    if (data?.code == 200){
                        data.data?.let {
                            if (it.isNew == true){
                                AppsFlyerUtil.postAF("CompleteRegistration")
                            }
                            JSUserInfoUtil.setUserInfo(it)
                            if (it.mustUpdate!=null && it.mustUpdate!! == "1"){
                                function.invoke(3)
                            }else {
                                function.invoke(0)
                            }
                        }
                    }else{
                        if (data != null) {
                            Toast.makeText(MyApplication.application,data.message,Toast.LENGTH_SHORT).show()
                        }
                        function.invoke(1)
                    }

                }


            })
    }

    fun maidian(scene_type: String,start_time: String) {
        val map: MutableMap<String, String> = HashMap()
        map["start_time"] =start_time
        map["end_time"] = DateUtil.getTimeFromLongYMDHMS(DateUtil.getServerTimestamp())!!
        map["scene_type"] = scene_type
        map["device_no"] = RiskUtil.getAndroidID().toString()
        map["appsFlyerId"] = AppsFlyerLib.getInstance().getAppsFlyerUID(MyApplication.application)?:""
        NetService.getNewService().addUserAction(map)
            .compose(NetUtil.applySchedulers())
            .subscribe(object : NetCallback<BaseResponseBean?>() {
                override fun businessFail(netErrorModel: NetErrorModel) {

                }

                override fun businessSuccess(data: BaseResponseBean?) {
                    MMKV.defaultMMKV().putBoolean("firstIn",false)
                }
            })
    }

    fun getProtocolUrlv2(){
        val map: MutableMap<String, String> = HashMap()
        NetService.getNewService().getProtocolUrlv2(map)
            .compose(NetUtil.applySchedulers())
            .subscribe(object : NetCallback<ProtocolUrlResponse?>() {
                override fun businessFail(netErrorModel: NetErrorModel) {}
                override fun businessSuccess(data: ProtocolUrlResponse?) {
                    if (data?.code == 200){
                        data.data?.let {
                            for (protocolUrlInfoResponse in it){
                                if (protocolUrlInfoResponse.protocalType == 1){
                                    MMKV.defaultMMKV().putString("URL1",protocolUrlInfoResponse.url)
                                } else if (protocolUrlInfoResponse.protocalType == 2){
                                    MMKV.defaultMMKV().putString("URL2",protocolUrlInfoResponse.url)
                                }
                            }
                        }
                    }
                }
            })
    }

    fun logout(){
        val map: MutableMap<String, String> = HashMap()
        NetService.getNewService().logout(map)
            .compose(NetUtil.applySchedulers())
            .subscribe(object : NetCallback<BaseResponseBean?>() {
                override fun businessFail(netErrorModel: NetErrorModel) {}
                override fun businessSuccess(data: BaseResponseBean?) {
                }
            })
    }


    fun uploadCreditModeLoanWardAuth(authInfoBean: AuthInfoBean, mWebView: WebView, id:String, event:String){
        val map: MutableMap<String, String> = HashMap()
        val content = Gson().toJson(authInfoBean)
        map["authInfo"] = Base64.encodeToString(content.toByteArray(), Base64.DEFAULT)
        NetService.getNewService().uploadCreditModeLoanWardAuth(map)
            .compose(NetUtil.applySchedulers())
            .subscribe(object : NetCallback<BaseResponseBean>() {
                override fun businessFail(netErrorModel: NetErrorModel) {
                    if (netErrorModel == null){
                        JSCallBack.callBackJsError(event,mWebView,id,"data is null")
                    }else{
                        JSCallBack.callBackJsError(event,mWebView,id,netErrorModel.message)
                    }
                }
                override fun businessSuccess(data: BaseResponseBean) {
                    if (data.code == 200){
                        JSCallBack.callBackJsSuccess(event,mWebView,id,"")
                    }else{
                        JSCallBack.callBackJsError(event,mWebView,id,data.message)
                    }
                }
            })
    }

    fun upImage(webView: WebView, id: String, file: File, action: String) {
        NetService.okHttpUploadImage(file, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                JSCallBack.callBackJsError(action,webView,id,e.toString())
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    try {
                        val responseBody: String = NetService.getResponseBody(response)
                        val imageResponse: UploadImgResponse = Gson().fromJson(responseBody, UploadImgResponse::class.java)
                        val jsInfoBean = JSDataModel.JSDataInfo()
                        jsInfoBean.value = imageResponse.data.toString()
                        JSCallBack.callBackJsSuccess(action,webView,id,Gson().toJson(jsInfoBean))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        JSCallBack.callBackJsError(action,webView,id,e.toString())
                    }
                } else {
                    JSCallBack.callBackJsError(action,webView,id)
                }
            }
        })
    }

    fun checkUpdate(function: Function1<Int,Int>){
        val map: MutableMap<String, String> = HashMap()
        map.put("version",RiskUtil.getVersionName())
        NetService.getNewService()
            .staticLogin(map)
            .compose(NetUtil.applySchedulers())
            .subscribe(object : NetCallback<UserInfoResponseBase?>(){
                override fun businessFail(netErrorModel: NetErrorModel?) {
                    JSUserInfoUtil.logout()
                }

                override fun businessSuccess(data: UserInfoResponseBase?) {
                    if (data?.code == 200) {
                        data?.data?.let {
                            JSUserInfoUtil.setUserInfo(it)
                            if (it.mustUpdate!=null ){
                                if (it.mustUpdate == "1" || it.mustUpdate == "0"){
                                    function.invoke(3)
                                }
                            }
                        }
                    }else{
                        JSUserInfoUtil.logout()
                    }
                }
            })
    }
}