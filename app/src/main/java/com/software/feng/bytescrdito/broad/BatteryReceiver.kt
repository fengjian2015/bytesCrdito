package com.software.feng.bytescrdito.broad

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import com.software.feng.bytescrdito.common.Cons
import java.util.*

/**
 * Time：2024/5/22
 * Author：feng
 * Description：
 */
class BatteryReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, intent: Intent?) {
        if (intent == null) return
        val status: Int = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val plugged: Int = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
        val health: Int = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)
        val voltage: Int = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
        val temperature: Int = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) / 10
        try {
            Cons.KEY_BATTERY_VOLTAGE = voltage
            val batteryTotal: Float = (intent.extras?.getInt("scale")?.toFloat() ?: 0) as Float
            val level: Float = (intent.extras?.getInt("level")?.toFloat() ?: 0) as Float
            if (level != null && batteryTotal != null) {
                Cons.KEY_BATTERY_LEVEL =String.format(Locale.getDefault(), "%.2f", level / batteryTotal)
                if (Cons.open_power === -1) {
                    Cons.open_power = (level / batteryTotal * 100).toInt()
                }
                Cons.complete_apply_power = (level / batteryTotal * 100).toInt()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (plugged == BatteryManager.BATTERY_PLUGGED_USB) {
            Cons.KEY_BATTERY_IS_USB = 1
        } else {
            Cons.KEY_BATTERY_IS_USB = 0
        }
        if (plugged == BatteryManager.BATTERY_PLUGGED_AC) {
            Cons.KEY_BATTERY_IS_AC = 1
        } else {
            Cons.KEY_BATTERY_IS_AC = 0
        }
        Cons.KEY_BATTERY_STATUS = status
        Cons.KEY_BATTERY_HEALTH = health
        Cons.KEY_BATTERY_TEMPER = temperature
    }
}