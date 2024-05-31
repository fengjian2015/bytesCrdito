package com.software.feng.bytescrdito

import android.app.Application
import com.software.feng.bytescrdito.common.Cons
import com.software.feng.bytescrdito.http.InstallReferrerManager
import com.software.feng.bytescrdito.util.ActivityManager
import com.software.feng.bytescrdito.util.AppsFlyerUtil
import com.software.feng.bytescrdito.util.DateUtil
import com.software.feng.utillibrary.FengLib
import com.tencent.mmkv.MMKV

/**
 * Time：2024/5/12
 * Author：feng
 * Description：
 */
class MyApplication : Application() {

    companion object{
       lateinit var application:MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        MMKV.initialize(this)
        AppsFlyerUtil.initAppsFlyer()
        InstallReferrerManager.init(application)
        ActivityManager.registerActivityLifecycleCallbacks()
        Cons.open_time = DateUtil.getTimeFromLongYMDHMS(DateUtil.getServerTimestamp())
        FengLib.setBaseUrl(Cons.baseUrl)
    }
}