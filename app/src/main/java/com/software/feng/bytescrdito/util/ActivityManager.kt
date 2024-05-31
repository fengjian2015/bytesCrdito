package com.software.feng.bytescrdito.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.software.feng.bytescrdito.MyApplication
import java.lang.ref.WeakReference


object ActivityManager {
    var back_num = 0
    var activityAount = 0
    private var sCurrentActivityWeakRef: WeakReference<Activity>? = null

    fun getCurrentActivity(): Activity? {
        var currentActivity: Activity? = null
        sCurrentActivityWeakRef?.let {
            currentActivity = it.get()!!
        }
        return currentActivity
    }

    fun registerActivityLifecycleCallbacks(){
        MyApplication.application?.registerActivityLifecycleCallbacks(object:Application.ActivityLifecycleCallbacks{
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
                sCurrentActivityWeakRef = WeakReference(p0)
            }

            override fun onActivityStarted(p0: Activity) {
                activityAount++
            }

            override fun onActivityResumed(p0: Activity) {
                sCurrentActivityWeakRef = WeakReference(p0)
            }

            override fun onActivityPaused(p0: Activity) {
            }

            override fun onActivityStopped(p0: Activity) {
                activityAount--
                if (activityAount == 0) {
                    //后台
                    back_num++
                }
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
            }

            override fun onActivityDestroyed(p0: Activity) {
            }

        })
    }

}