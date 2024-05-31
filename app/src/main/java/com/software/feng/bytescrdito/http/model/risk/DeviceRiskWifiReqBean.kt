package com.software.feng.bytescrdito.http.model.risk

/**
 * Time：2024/5/20
 * Author：feng
 * Description：
 */
class DeviceRiskWifiReqBean : java.io.Serializable {
    //wifi名称   （对应安卓是ssid）
    var wifi_name: String? = ""

    //wifi BSSID   （是路由器mac地址）
    var bssid: String? = ""

    //wifi类型 1当前wifi 2配置wifi
    var type: Int? = null

    //wifi mac地址  （设备MAC） ps:安卓6以上获取不到配置mac 地址，只能获取正在连接地址（固定为02:00:00:00)。
    var mac: String? = ""
    override fun toString(): String {
        return "DeviceRiskWifiReqBean(wifi_name=$wifi_name, bssid=$bssid, type=$type, mac=$mac)"
    }


}