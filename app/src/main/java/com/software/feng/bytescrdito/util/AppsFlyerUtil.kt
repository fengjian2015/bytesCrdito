package com.software.feng.bytescrdito.util

import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.software.feng.bytescrdito.BuildConfig
import com.software.feng.bytescrdito.MyApplication
import com.software.feng.bytescrdito.common.Cons
import com.software.feng.bytescrdito.js.data.JSUserInfoUtil
import com.software.feng.utillibrary.util.LogUtil
import com.tencent.mmkv.MMKV

object AppsFlyerUtil {

    fun postAF(evenName: String?) {
        var evenName = evenName
        val map = HashMap<String, Any?>()
        val loadId: String
        if (evenName != null && evenName.contains("|")) {
            val split = evenName.split("\\|".toRegex()).toTypedArray()
            loadId = split[1]
            evenName = split[0]
            map["loan_id"] = loadId
        }
        map["mobile"] = JSUserInfoUtil.getPhone()
        map["event_code"] = evenName
        AppsFlyerLib.getInstance().logEvent(MyApplication.application, evenName, map)
    }

    fun initAppsFlyer() {
        AppsFlyerLib.getInstance().init("r9zAMR8qvrbFheqr6crjDj", object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(map: Map<String, Any>) {
                LogUtil.d("AppsFlyerLib Success: $map")
                for (attrName in map.keys) {
                    if ("af_status" == attrName) {
                        val data = map[attrName] as String?
                        if ("Organic" == data) {
                            MMKV.defaultMMKV().putString(Cons.KEY_AF_CHANNEL, data)
                        } else if ("Non-organic" == data) {
                            MMKV.defaultMMKV().putString(Cons.KEY_AF_CHANNEL, map["media_source"].toString())
                        }
                    }
                }
            }

            override fun onConversionDataFail(s: String) {
                LogUtil.d("AppsFlyerLib DataFail: $s")
            }

            override fun onAppOpenAttribution(map: Map<String, String>) {
                LogUtil.d("AppsFlyerLib Attribution: $map")
            }

            override fun onAttributionFailure(s: String) {
                LogUtil.d("AppsFlyerLib AttributionFailure: $s")
            }
        }, MyApplication.application)
        AppsFlyerLib.getInstance().start(MyApplication.application, "r9zAMR8qvrbFheqr6crjDj", object : AppsFlyerRequestListener {
            override fun onSuccess() {
                LogUtil.d("AppsFlyerLib start Success ")
            }

            override fun onError(i: Int, s: String) {
                LogUtil.d("AppsFlyerLib start Error $s")
            }
        })
        AppsFlyerLib.getInstance().setDebugLog(BuildConfig.DEBUG)
    }
}