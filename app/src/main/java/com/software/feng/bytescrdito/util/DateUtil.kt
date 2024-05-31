package com.software.feng.bytescrdito.util

import android.os.SystemClock
import android.text.TextUtils
import com.tencent.mmkv.MMKV
import java.text.SimpleDateFormat

/**
 * Time：2024/5/20
 * Author：feng
 * Description：
 */
object DateUtil {
    const val FMT_DATE_TIME = "yyyy-MM-dd HH:mm:ss"
    const val FMT_DATE_TIME1 = "yyyy-MM-dd,HH,mm,ss"
    const val FMT_DATE_TIME2 = "HH:mm:ss"
    const val KEY_SERVICE_TIME1 = "KEY_SERVICE_TIME1"
    const val KEY_DIFFERENCE_TIME1 = "KEY_DIFFERENCE_TIME1"

    fun initTime(s: String) {
        if (TextUtils.isEmpty(s)) return
        var serviceTimestamp: Long = 0
        try {
            serviceTimestamp = s.toLong()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        if (serviceTimestamp == 0L) {
            return
        }
        val elapsedRealtime = SystemClock.elapsedRealtime()
        MMKV.defaultMMKV().putLong(KEY_SERVICE_TIME1,serviceTimestamp)
        MMKV.defaultMMKV().putLong(KEY_DIFFERENCE_TIME1,elapsedRealtime)
    }

    fun getTimeFromLongHMS(time: Long): String {
        try {
            return SimpleDateFormat(FMT_DATE_TIME2).format(time)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun getTimeFromLongYMDHMS(time: Long): String {
        try {
            return SimpleDateFormat(FMT_DATE_TIME).format(time)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 获取服务器时间戳
     */
    fun getServerTimestamp(): Long {
        val aLong: Long = MMKV.defaultMMKV().getLong(KEY_SERVICE_TIME1,0)
        val elapsedRealtime: Long = MMKV.defaultMMKV().getLong(KEY_DIFFERENCE_TIME1,0)
        return if (aLong < 10) {
            System.currentTimeMillis()
        } else {
            SystemClock.elapsedRealtime() - elapsedRealtime + aLong
        }
    }


}