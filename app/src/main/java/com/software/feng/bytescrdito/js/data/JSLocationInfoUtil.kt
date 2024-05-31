package com.software.feng.bytescrdito.js.data

import android.text.TextUtils
import android.webkit.WebView
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.software.feng.bytescrdito.MyApplication
import com.software.feng.bytescrdito.common.Cons
import com.software.feng.bytescrdito.http.NetRequestManage
import com.software.feng.bytescrdito.http.model.risk.AuthGpsBean
import com.software.feng.bytescrdito.http.model.risk.AuthInfoBean
import com.software.feng.bytescrdito.http.model.risk.GpsLocationInfoBean
import com.software.feng.bytescrdito.js.JSCallBack
import com.software.feng.bytescrdito.observer.ObserverManager
import com.software.feng.bytescrdito.observer.ObserverType
import com.software.feng.bytescrdito.util.ActivityManager
import com.software.feng.bytescrdito.util.DateUtil
import com.software.feng.bytescrdito.util.LocationListenerUtil
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
object JSLocationInfoUtil {
    fun locationInfo(id: String, webView: WebView) {
        RiskUtil.openLocService()
        XXPermissions.with(ActivityManager.getCurrentActivity()!!)
            .permission(Permission.ACCESS_COARSE_LOCATION)
            .permission(Permission.READ_PHONE_STATE)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    if (!RiskUtil.isLocServiceEnable()) {
                        JSCallBack.callbackJsErrorPermissions(webView,id, Cons.InvokeCreditoLocationInfo)
                        return
                    }
                    if (allGranted){
                        ObserverManager.getManager().sendNotify(ObserverType.INIT_LOCATION_LISTENER)
                        onGranted(id,webView)
                    }else{
                        JSCallBack.callbackJsErrorPermissions(webView,id, Cons.InvokeCreditoLocationInfo)
                    }
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    JSCallBack.callbackJsErrorPermissions(webView,id, Cons.InvokeCreditoLocationInfo)
                }
            })
    }

    fun onGranted(id: String, webView: WebView){
        GlobalScope.launch(Dispatchers.IO){

            var locationBean = AuthGpsBean()
            var location = RiskUtil.getLocation()
            if (location == null){
                for (i in 0 .. 2){
                    try {
                        Thread.sleep(2000)
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                    location = LocationListenerUtil.getLastKnownLocation()
                }
            }
            locationBean.create_time = DateUtil.getServerTimestamp()
            var gps = GpsLocationInfoBean()
            location?.let {
                gps.latitude = it.latitude.toString()
                gps.longitude = it.longitude.toString()
                locationBean.device_id = RiskUtil.getAndroidID()
                locationBean.gps = gps
                val address = RiskUtil.getAddress(location.latitude, location.longitude)
                address?.let {
                    locationBean.gps_address_province =address.adminArea
                    locationBean.gps_address_city = address.locality
                    if (TextUtils.isEmpty(address.featureName)) {
                        address.featureName = address.getAddressLine(0)
                    }
                    if (TextUtils.isEmpty(address.featureName)) {
                        address.featureName = address.subAdminArea
                    }
                    if (TextUtils.isEmpty(address.featureName)) {
                        address.featureName = address.thoroughfare
                    }
                    if (TextUtils.isEmpty(address.thoroughfare)) {
                        address.thoroughfare = address.featureName
                    }
                    locationBean.gps_address_street = address.thoroughfare
                    locationBean.gps_address_address =address.featureName
                }
            }
            withContext(Dispatchers.Main){
                locationBean.gps?.latitude =
                    locationBean.gps?.latitude?.let { it1 ->
                        RiskUtil.roundDown(it1,2)?.let { it1 ->
                            RiskUtil.toDouble(
                                it1
                            ).toString()
                        }
                    }
                locationBean.gps?.longitude =
                    locationBean.gps?.longitude?.let { it1 ->
                        RiskUtil.roundDown(it1,2)?.let { it1 ->
                            RiskUtil.toDouble(
                                it1
                            ).toString()
                        }
                    }
                LogUtil.d("位置：$locationBean")
                var applyInfoBean = AuthInfoBean()
                applyInfoBean.gps = locationBean
                NetRequestManage.uploadCreditModeLoanWardAuth(applyInfoBean,webView,id,Cons.InvokeCreditoLocationInfo)
            }
        }
    }
}