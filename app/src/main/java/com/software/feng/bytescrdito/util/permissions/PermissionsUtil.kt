package com.software.feng.bytescrdito.util.permissions

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import com.software.feng.bytescrdito.MyApplication

/**
 * Time：2024/5/20
 * Author：feng
 * Description：
 */
object PermissionsUtil {
    fun openLocService() {
        if (isLocServiceEnable()) return
        val intent = Intent()
        intent.action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        MyApplication.application.startActivity(intent)
    }

    fun isLocServiceEnable(): Boolean {
        val locationManager = MyApplication.application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun openWifi() {
        val wifiManager = MyApplication.application.applicationContext.getSystemService(
            Context.WIFI_SERVICE
        ) as WifiManager
        if (!wifiManager.isWifiEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val i = Intent(Settings.ACTION_WIFI_SETTINGS)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                MyApplication.application.startActivity(i)
            } else {
                wifiManager.isWifiEnabled = true
            }
        }
    }

    fun isOpenWifi(): Boolean {
        val wifiManager = MyApplication.application.applicationContext.getSystemService(
            Context.WIFI_SERVICE
        ) as WifiManager
        return wifiManager.isWifiEnabled
    }
}