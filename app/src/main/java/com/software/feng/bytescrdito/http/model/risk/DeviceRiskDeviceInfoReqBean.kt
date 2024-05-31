package com.software.feng.bytescrdito.http.model.risk

/**
 * Time：2024/5/20
 * Author：feng
 * Description：
 */
class DeviceRiskDeviceInfoReqBean(var isRooted : String ? = "") : java.io.Serializable {
    //是否root  "true" "false"
    override fun toString(): String {
        return "DeviceRiskDeviceInfoReqBean(isRooted=$isRooted)"
    }
}