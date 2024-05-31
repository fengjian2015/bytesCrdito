package com.software.feng.bytescrdito.js.data

import android.annotation.SuppressLint
import android.os.Build
import android.os.SystemClock
import android.webkit.WebView
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.software.feng.bytescrdito.MyApplication
import com.software.feng.bytescrdito.common.Cons
import com.software.feng.bytescrdito.http.NetRequestManage
import com.software.feng.bytescrdito.http.model.risk.*
import com.software.feng.bytescrdito.js.JSCallBack
import com.software.feng.bytescrdito.js.model.JSDataModel
import com.software.feng.bytescrdito.observer.ObserverManager
import com.software.feng.bytescrdito.observer.ObserverType.INIT_LOCATION_LISTENER
import com.software.feng.bytescrdito.util.*
import com.software.feng.bytescrdito.util.permissions.PermissionsUtil
import com.software.feng.utillibrary.util.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Time：2024/5/19
 * Author：feng
 * Description：
 */
object JSDeviceInfoUtil {
    fun deviceInfo(id: String, webView: WebView, data: JSDataModel.JSDataInfo?) {
        PermissionsUtil.openLocService()
        PermissionsUtil.openWifi()
        XXPermissions.with(ActivityManager.getCurrentActivity()!!)
            .permission(Permission.ACCESS_COARSE_LOCATION)
            .permission(Permission.READ_PHONE_STATE)
            .request(object : OnPermissionCallback{
                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    if (!PermissionsUtil.isLocServiceEnable() || !PermissionsUtil.isOpenWifi()) {
                        JSCallBack.callbackJsErrorPermissions(webView,id, Cons.InvokeCreditoDeviceInfo)
                        return
                    }
                    if (allGranted){
                        ObserverManager.getManager().sendNotify(INIT_LOCATION_LISTENER)
                        onGranted(id,webView,data)
                    }else{
                        JSCallBack.callbackJsErrorPermissions(webView,id, Cons.InvokeCreditoDeviceInfo)
                    }
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    super.onDenied(permissions, doNotAskAgain)
                    JSCallBack.callbackJsErrorPermissions(webView,id, Cons.InvokeCreditoDeviceInfo)
                }
            })
    }

    @SuppressLint("MissingPermission")
    private fun onGranted(id: String, webView: WebView, data: JSDataModel.JSDataInfo?){
        GlobalScope.launch(Dispatchers.IO){
            LogUtil.d("抓取位置中！！！！")
            Thread.sleep(3555)
            var authDeviceInfoBean = AuthDeviceInfoBean()
            authDeviceInfoBean.create_time = DateUtil.getServerTimestamp() / 1000
            authDeviceInfoBean.VideoExternal = RiskUtil.getVideoExternalFiles().size
            authDeviceInfoBean.phone_brand = Build.BRAND
            authDeviceInfoBean.cur_wifi_mac = RiskUtil.getWifiInfo()
            authDeviceInfoBean.imei2 =RiskUtil.getIMEI1()
            authDeviceInfoBean.imei1 =RiskUtil.getIMEI1()
            authDeviceInfoBean.build_fingerprint =Build.FINGERPRINT
            authDeviceInfoBean.cur_wifi_ssid = RiskUtil.getWifiName()
            authDeviceInfoBean.DownloadFiles = RiskUtil.getDownloadFiles().size
            authDeviceInfoBean.time_zoneId = RiskUtil.getTimeZoneId()
            authDeviceInfoBean.kernel_version = RiskUtil.getKernelVersion()
            authDeviceInfoBean.currentSystemTime = (System.currentTimeMillis() / 1000).toString()
            authDeviceInfoBean.AudioInternal = RiskUtil.getAudioInternalFiles()?.size.toString()
            authDeviceInfoBean.nettype = RiskUtil.getNetworkState().toString()
            authDeviceInfoBean.serial =  if ("unknown" == Build.SERIAL) null else Build.SERIAL
            authDeviceInfoBean.android_id = RiskUtil.getAndroidID()
            authDeviceInfoBean.kernel_architecture = Build.CPU_ABI
            authDeviceInfoBean.build_id = Build.ID
            authDeviceInfoBean.ImagesInternal = RiskUtil.getImagesInternalFiles()?.size.toString()
            authDeviceInfoBean.build_number = Build.DISPLAY
            authDeviceInfoBean.mac =MACUtil.getMacAddress()
            authDeviceInfoBean.board = Build.BOARD
            authDeviceInfoBean.VideoInternal = RiskUtil.getVideoInternalFiles()?.size ?: 0
            authDeviceInfoBean.AudioExternal = RiskUtil.getAudioExternalFiles()?.size ?: 0
            authDeviceInfoBean.build_time = Build.TIME
            authDeviceInfoBean.wifiCount = RiskUtil.getWifiList()?.size ?: 0
            authDeviceInfoBean.time_zone = RiskUtil.getTimeZone()
            authDeviceInfoBean.release_date = Build.TIME
            authDeviceInfoBean.device_name = RiskUtil.getDeviceName()
            authDeviceInfoBean.ImagesExternal = RiskUtil.getImagesExternalFiles()?.size.toString()
            authDeviceInfoBean.security_patch_level = Build.VERSION.SECURITY_PATCH
            authDeviceInfoBean.sensorCount = RiskUtil.getSensorCount()
            authDeviceInfoBean.wifi_state = RiskUtil.getCurrentNetworkRssi()
            authDeviceInfoBean.gaid = RiskUtil.getGAID()
            authDeviceInfoBean.back_num = ActivityManager.back_num
            authDeviceInfoBean.open_time = Cons.open_time
            authDeviceInfoBean.complete_time = DateUtil.getTimeFromLongYMDHMS(DateUtil.getServerTimestamp())
            authDeviceInfoBean.open_power = Cons.open_power
            authDeviceInfoBean.complete_apply_power = Cons.complete_apply_power
            authDeviceInfoBean.telephony = RiskUtil.getCarrierName()
            authDeviceInfoBean.network_status = RiskUtil.getNetworkState1()
            authDeviceInfoBean.dbm= Cons.dbm.toString()
            authDeviceInfoBean.is_contain_sd = RiskUtil.checkSDK().toString()
            authDeviceInfoBean.front_camera_pixels =RiskUtil.getFrontCameraResolution()
            authDeviceInfoBean.screen_brightness = RiskUtil.getScreenBrightness().toString()
            authDeviceInfoBean.sim_card_type = RiskUtil.getSimCardType()
            authDeviceInfoBean.silent_mode = RiskUtil.getPhoneMode().toString()
            authDeviceInfoBean.rear_camera_pixels = RiskUtil.getBackCameraResolution()
            authDeviceInfoBean.network_operator_country_code  = RiskUtil.getNetworkOperatorCountryCode()
            authDeviceInfoBean.network_operator_code = RiskUtil.getSimOperator()
            authDeviceInfoBean.locale_country = Locale.getDefault().country
            authDeviceInfoBean.locale_display_country = Locale.getDefault().displayCountry
            authDeviceInfoBean.airplane_mode = RiskUtil.isFlightModeEnabled()
            authDeviceInfoBean.bluetooth_flag = RiskUtil.getBluetoothEnable().toString()
            authDeviceInfoBean.is_dualcard_standby = RiskUtil.isDualcardStandby()
            authDeviceInfoBean.wifi_hotspot = RiskUtil.wifiHotspot()
            authDeviceInfoBean.is_insert_expansion_card = RiskUtil.isInsertExpansionCard()
            authDeviceInfoBean.version_number = RiskUtil.getVersionCode().toString()
            authDeviceInfoBean.version_name = RiskUtil.getVersionName()
            authDeviceInfoBean.cpu_num =Runtime.getRuntime().availableProcessors()
            authDeviceInfoBean.wifiList =RiskUtil.getWifiList()

            var deviceStorageBean = DeviceStorageBean()
            deviceStorageBean.availableDiskSize = RiskUtil.getAvailableSpace()
            deviceStorageBean.availableMemory = RiskUtil.getAvailMemory().toString()
            deviceStorageBean.elapsedRealtime = SystemClock.elapsedRealtime().toString()
            deviceStorageBean.totalBootTimeWak =SystemClock.uptimeMillis().toString()
            deviceStorageBean.isUSBDebug = RiskUtil.isUsbDebug()
            deviceStorageBean.isUsingProxyPort = RiskUtil.isProxy()
            deviceStorageBean.isUsingVPN = RiskUtil.isVpn()
            deviceStorageBean.memorySize = RiskUtilJava.getTotalMemory().toString()
            deviceStorageBean.memoryUsableSize = RiskUtil.readSDCardMemoryUsableSize().toString()
            deviceStorageBean.memoryUseSize = RiskUtil.readSDCardMemoryUseSize().toString()
            deviceStorageBean.ram_total_size = RiskUtilJava.getTotalMemory().toString()
            deviceStorageBean.totalDiskSize = RiskUtil.getTotalRam()
            deviceStorageBean.ramUsedSize = (RiskUtilJava.getTotalMemory() - RiskUtil.getAvailMemory()).toString()
            deviceStorageBean.appMaxMemory = RiskUtil.appMaxMemory().toString()
            deviceStorageBean.appAvailableMemory = RiskUtil.getAvailableSize().toString()
            deviceStorageBean.app_free_memory =  RiskUtil.app_free_memory().toString()
            authDeviceInfoBean.storage = deviceStorageBean

            authDeviceInfoBean.sensors = RiskUtil.getSensorsList()
            authDeviceInfoBean.wifiInfos = RiskUtil.getDeviceRiskWifiReqBeanList()
            authDeviceInfoBean.general_data =RiskUtil.getDeviceRiskDeviceGeneralReqBean()
            authDeviceInfoBean.hardware =RiskUtil.getDeviceRiskHardwareReqBean()
            if (data != null) {
                authDeviceInfoBean.public_ip = DeviceRiskDevicePublicIPReqBean(data.ip)
            }
            authDeviceInfoBean.battery_status = RiskUtil.getDeviceRiskBatteryInfoReqBean()
            authDeviceInfoBean.device_info = DeviceRiskDeviceInfoReqBean( if (RiskUtil.isDeviceRooted()) "true" else "false")
            withContext(Dispatchers.Main){
                LogUtil.d("设备数据加载完成：$authDeviceInfoBean")
                val authInfoBean = AuthInfoBean()
                authInfoBean.device_info  = authDeviceInfoBean
                NetRequestManage.uploadCreditModeLoanWardAuth(authInfoBean,webView,id,Cons.InvokeCreditoDeviceInfo)
            }
        }
    }
}