package com.software.feng.bytescrdito.common

import android.content.Context

/**
 * Time：2024/5/19
 * Author：feng
 * Description：
 */
object CommonUtil {
    fun getVersionName(context: Context?): String {
        if (context == null) {
            return ""
        }
        var versionName = ""
        try {
            versionName =
                context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return versionName
    }
}