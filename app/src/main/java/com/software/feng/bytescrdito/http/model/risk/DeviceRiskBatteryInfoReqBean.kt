package com.software.feng.bytescrdito.http.model.risk

/**
 * Time：2024/5/20
 * Author：feng
 * Description：
 */
class DeviceRiskBatteryInfoReqBean : java.io.Serializable {
    //是否 usb 充电，0：否，1：是
    var is_usb_charge = 0

    //是否交流充电，0：否，1：是
    var is_ac_charge = 0

    //string	电池百分比
    var batteryPercentage: String? = ""

    //电池状态
    var battery_temper: String? = ""

    //电池寿命
    var battery_health: String? = ""

    //是否正在充电，UNKNOWN： 1，CHARGING：2，DISCHARGING：3
    var batteryStatus: String? = ""

    //获取电量的时间 到毫秒
    var power_time: Long = 0

    //设备ID
    var device_id: String? = ""

    //电池电压
    var voltage: String? = ""

    //获取电池容量mAh  （单位mAh）
    var battery_capacity: String? = ""
    override fun toString(): String {
        return "DeviceRiskBatteryInfoReqBean(is_usb_charge=$is_usb_charge, is_ac_charge=$is_ac_charge, batteryPercentage=$batteryPercentage, battery_temper=$battery_temper, battery_health=$battery_health, batteryStatus=$batteryStatus, power_time=$power_time, device_id=$device_id, voltage=$voltage, battery_capacity=$battery_capacity)"
    }


}