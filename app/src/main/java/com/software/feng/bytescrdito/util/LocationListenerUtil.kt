package com.software.feng.bytescrdito.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.software.feng.bytescrdito.MyApplication
import com.software.feng.utillibrary.util.LogUtil

/**
 * Time：2024/5/20
 * Author：feng
 * Description：
 */
object LocationListenerUtil {
    private var locationManager: LocationManager? = null

    fun getLocationManager(): LocationManager? {
        MyApplication.application?.let {
            if (locationManager == null) {
                locationManager =
                    it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            }
            return locationManager
        }

        return null
    }

    @SuppressLint("MissingPermission")
    public fun getLastKnownLocation(): Location? {
        getLocationManager()?.let {
            var lastLocation: Location? = null
            try {
                var l: Location? = it.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                if (l != null) {
                    return l
                }
                val providers: List<String> = it.getProviders(true)
                for (provider in providers) {
                    l = it.getLastKnownLocation(provider)
                    if (l == null) {
                        continue
                    }
                    if (lastLocation == null || l.accuracy < lastLocation.accuracy) {
                        lastLocation = l
                    }
                }
                return lastLocation

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return null
    }


    private val mLocationListener: LocationListener = object : LocationListener {
        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            LogUtil.d("onStatusChanged")
        }

        // Provider被enable时触发此函数，比如GPS被打开
        override fun onProviderEnabled(provider: String) {
            LogUtil.d("onProviderEnabled")
        }

        // Provider被disable时触发此函数，比如GPS被关闭
        override fun onProviderDisabled(provider: String) {
            LogUtil.d("onProviderDisabled")
        }

        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @SuppressLint("MissingPermission")
        override fun onLocationChanged(location: Location) {
            LogUtil.d(
                String.format(
                    "location: longitude: %f, latitude: %f", location.longitude,
                    location.latitude
                )
            )
            //更新位置信息
            locationManager!!.removeUpdates(this)
        }
    }


    /**
     * 监听位置变化
     */
    @SuppressLint("MissingPermission")
    fun initLocationListener() {
        LogUtil.d("监听定位信息")
        locationManager = MyApplication.application.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        if (locationManager == null) {
            return
        }
        locationManager!!.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            1000,
            10f,
            mLocationListener
        )
    }
}