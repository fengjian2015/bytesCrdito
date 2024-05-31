package com.software.feng.bytescrdito.http.model.risk

/**
 * Time：2024/5/20
 * Author：feng
 * Description：
 */
class DeviceRiskDeviceGeneralReqBean : java.io.Serializable {

    //指示设备电话类型的常量 NONE：0，GS：1，CDMA：2，SIP=3"language
    var phone_type: String? = ""

    //系统语言
    var language: String? = ""

    //用户显示的语言环境语音的名称
    var locale_display_language: String? = ""

    //网络运营商名称	TELCEL
    var network_operator_name: String? = ""

    //此地区的国家地区缩写
    var locale_iso_3_country: String? = ""

    //语言环境的字母缩写	eng
    var locale_iso_3_language: String? = ""

    //最后一次启动时间，毫秒时间戳
    var last_boot_time: String? = ""

    //是否模拟器(0/1)
    var is_simulator: String? = ""

    //语言(国家)(全称) 参考值:English (India)
    var locale_display_name : String? = ""
    override fun toString(): String {
        return "DeviceRiskDeviceGeneralReqBean(phone_type=$phone_type, language=$language, locale_display_language=$locale_display_language, network_operator_name=$network_operator_name, locale_iso_3_country=$locale_iso_3_country, locale_iso_3_language=$locale_iso_3_language, last_boot_time=$last_boot_time, is_simulator=$is_simulator, locale_display_name=$locale_display_name)"
    }

}