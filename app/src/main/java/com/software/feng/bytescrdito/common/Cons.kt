package com.software.feng.bytescrdito.common

/**
 * Time：2024/5/12
 * Author：feng
 * Description：
 */
object Cons {
    //======================可变数据====================================
    var open_time : String =""
    var open_power = -1 //打开app时的电量，比如 70%
    var complete_apply_power = 0 //提交申请时电量  ，比如70% ，= 0
    var dbm: Int = 0

    var KEY_BATTERY_STATUS = 0
    var KEY_BATTERY_IS_USB = 0
    var KEY_BATTERY_IS_AC = 0
    var KEY_BATTERY_LEVEL = ""
    var KEY_BATTERY_HEALTH = 0
    var KEY_BATTERY_TEMPER = 0
    var KEY_BATTERY_VOLTAGE = 0

    //======================数据====================================
    const val JavascriptInterfaceName = "mx"
    const val baseUrl = "http://118.31.58.168:7004"
    const val UPLOADIMAGE="/app/system/uploadimg"

    //======================User KEY====================================
    const val KEY_USER_INFO="KEY_USER_INFO"
    val KEY_AF_CHANNEL ="KEY_AF_CHANNEL"
    val EXTENSION = "EXTENSION"
    const val SELECT_CONTACTS_CONTRACT = 1088
    const val TACK_PHOTO = 1888
    //======================JS KEY====================================

    const val InvokeCreditoUserInfo = "invokeCreditoUserInfo"
    const val InvokeCreditoSignOut = "invokeCreditoSignOut"
    const val InvokeCreditoDeviceInfo ="invokeCreditoDeviceInfo"
    const val InvokeCreditoInstallationInfo ="invokeCreditoInstallationInfo"
    const val InvokeCreditoSmsInfo = "invokeCreditoSmsInfo"
    const val InvokeCreditoLocationInfo = "invokeCreditoLocationInfo"
    const val InvokeCreditoCallLog = "invokeCreditoCallLog"
    const val InvokeCreditoCalendarInfo = "invokeCreditoCalendarInfo"
    const val InvokeCreditoSelectContact = "invokeCreditoSelectContact"
    const val InvokeCreditoAppsFlyer = "invokeCreditoAppsFlyer"
    const val InvokeCreditoTackPhoto = "invokeCreditoTackPhoto"
    const val InvokeCreditoForwardOutside = "invokeCreditoForwardOutside"
    const val InvokeCreditoAppServiceTime = "invokeCreditoAppServiceTime"
    const val InvokeCreditoOpenBrowser = "invokeCreditoOpenBrowser"

}