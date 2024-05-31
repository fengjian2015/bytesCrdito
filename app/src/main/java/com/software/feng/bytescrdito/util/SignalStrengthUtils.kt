package com.software.feng.bytescrdito.util

import android.content.Context
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.SignalStrength
import android.telephony.TelephonyManager

object SignalStrengthUtils {

    interface SignalStrengthListener {
        fun onSignalStrengthChanged(signalStrength: Int)
    }

    fun getPhoneSignalStrength(context: Context, listener: SignalStrengthListener) {
        val telephonyManager =
            context.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val phoneStateListener = object : PhoneStateListener() {
            override fun onSignalStrengthsChanged(signalStrength: SignalStrength) {
                super.onSignalStrengthsChanged(signalStrength)

                // 获取信号强度
                val dBm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    signalStrength.cellSignalStrengths[0].dbm
                } else {
                    getSignalStrengthValue(signalStrength)
                }

                // 通知监听器
                listener.onSignalStrengthChanged(dBm)
            }
        }

        // 注册监听器
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)

        // 获取初始信号强度并通知监听器
        listener.onSignalStrengthChanged(0) // 你可能需要传递一个默认值
    }

    // 在API Level 28以下，通过反射获取信号强度值
    private fun getSignalStrengthValue(signalStrength: SignalStrength): Int {
        return try {
            val getDbmMethod = SignalStrength::class.java.getDeclaredMethod("getDbm")
            getDbmMethod.invoke(signalStrength) as Int
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }


}
