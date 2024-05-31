package com.software.feng.bytescrdito.http.model.risk

/**
 * Time：2024/5/20
 * Author：feng
 * Description：
 */
class AuthDeviceInfoBean : java.io.Serializable {

    //抓取时间-时间戳 13位时间戳
    var create_time: Long? = 0

    //视频外部文件个数
    var VideoExternal: Int? = 0

    //手机品牌
    var phone_brand: String? = null

    //最近WiFi的mac地址
    var cur_wifi_mac: String? = null

    //设备号2
    var imei2: String? = null

    //设备号1
    var imei1: String? = null

    //指纹信息
    var build_fingerprint: String? = null

    //最近WiFi的WLAN网络名称
    var cur_wifi_ssid: String? = null

    //下载文件个数
    var DownloadFiles = 0

    //string	时区id
    var time_zoneId: String? = null

    //内核版本
    var kernel_version: String? = null

    //设备当前时间
    var currentSystemTime: String? = null

    //音频内部文件个数
    var AudioInternal: String? = null

    //网络类型 unknown：0，GPRS：1，EDGE： 2，UMTS：3，CDMA: Either IS95A or IS95B：4，EVDO revision 0：5	3
    var nettype: String? = null

    //序列号
    var serial: String? = null

    //安卓id
    var android_id: String? = null

    //内核架构
    var kernel_architecture: String? = null

    //版本id
    var build_id: String? = null

    //图片内部文件个数
    var ImagesInternal: String? = null

    //版本号
    var build_number: String? = null

    //mac地址
    var mac: String? = null

    //主板
    var board: String? = null

    //视频内部文件个数
    var VideoInternal = 0

    //外部文件个数
    var AudioExternal = 0

    //版本日期
    var build_time: Long? = 0

    //WiFi个数
    var wifiCount = 0

    //string	时区
    var time_zone: String? = null

    //更新日期
    var release_date: Long? = 0

    //设备名称
    var device_name: String? = null

    //图片外部文件个数
    var ImagesExternal: String? = null

    //安全补丁时间  yyyy-mm-dd
    var security_patch_level: String? = null

    //传感器数量-N
    var sensorCount: String? = null

    //wifi强度
    var wifi_state: String? = null

    //谷歌id
    var gaid: String? = null

    //退到后台次数
    var back_num = 0

    //打开app时间yyyy-MM-dd HH:mm:ss
    var open_time: String? = null

    //提交申请时间  yyyy-MM-dd HH:mm:ss
    var complete_time: String? = null

    //打开app时的电量，比如 70% ， 就传入70
    var open_power = 0

    //提交申请时电量  ，比如70% ， 就传入70
    var complete_apply_power = 0

    //新增
    //运营商(MTN NG)
    var telephony: String? = null

    //网络类型（4g、5g)
    var network_status: String? = null

    //手机的信号强度(-92)
    var dbm: String? = null

    //是否有SD卡， 1-是， 0-否
    var is_contain_sd: String? = null

    //前置摄像头像素(数据库没有)（N）
    var front_camera_pixels: String? = null

    //获得屏幕亮度值是0~255之间（Y）
    var screen_brightness: String? = null

    //SIM卡类型:CHINA_MOBILE,CHINA_UNICOM,CHINA_TELCOM,other（Y）
    var sim_card_type: String? = null

    //手机模式:0未知1.普通模式2.振动模式3.静音模式（Y）
    var silent_mode: String? = null

    //后置摄像头像素（N）
    var rear_camera_pixels: String? = null

    //网络运营商归属国家编号。参考值：460（中国）,404，405(印度）（Y）
    var network_operator_country_code: String? = null

    //网络运营商具体编号。参考值：840（Jio)（Y）
    var network_operator_code: String? = null

    //国家缩写 参考值：IN（Y）
    var locale_country: String? = null

    //国家 参考值：India（Y）
    var locale_display_country: String? = null

    //是否飞行模式0不是，1是（N）
    var airplane_mode: String? = null

    //蓝牙是否启用:1打开，2关闭，0没有该组件（Y）
    var bluetooth_flag: String? = null

    //是否支持双卡双待(获取有效卡槽数)0不支持1支持（Y）
    var is_dualcard_standby: String? = null

    //热点是否开启0关闭，1开启（Y）
    var wifi_hotspot: String? = null

    //有无插入扩展卡0没有，1有（Y）
    var is_insert_expansion_card: String? = null

    //APP版本号（Y）
    var version_number: String? = null

    //APP版本名称（Y）
    var version_name: String? = null

    //cup核数
    var cpu_num : Int = 0

    //注册时环境wifi列表
    var wifiList: List<String>? = null

    //设备内存情况
    var storage: com.software.feng.bytescrdito.http.model.risk.DeviceStorageBean? = null

    //传感器
    var sensors: List<com.software.feng.bytescrdito.http.model.risk.DeviceSensorsBean>? = null

    //wifi列表
    var wifiInfos: List<com.software.feng.bytescrdito.http.model.risk.DeviceRiskWifiReqBean>? = null

    //系统信息
    var general_data: com.software.feng.bytescrdito.http.model.risk.DeviceRiskDeviceGeneralReqBean? = null

    //设备硬件信息
    var hardware: com.software.feng.bytescrdito.http.model.risk.DeviceRiskHardwareReqBean? = null

    //公网ip
    var public_ip: com.software.feng.bytescrdito.http.model.risk.DeviceRiskDevicePublicIPReqBean? = null

    //电池信息
    var battery_status: com.software.feng.bytescrdito.http.model.risk.DeviceRiskBatteryInfoReqBean? = null

    //设备信息
    var device_info: com.software.feng.bytescrdito.http.model.risk.DeviceRiskDeviceInfoReqBean? = null

    override fun toString(): String {
        return "AuthDeviceInfoBean(create_time=$create_time, VideoExternal=$VideoExternal, phone_brand=$phone_brand, cur_wifi_mac=$cur_wifi_mac, imei2=$imei2, imei1=$imei1, build_fingerprint=$build_fingerprint, cur_wifi_ssid=$cur_wifi_ssid, DownloadFiles=$DownloadFiles, time_zoneId=$time_zoneId, kernel_version=$kernel_version, currentSystemTime=$currentSystemTime, AudioInternal=$AudioInternal, nettype=$nettype, serial=$serial, android_id=$android_id, kernel_architecture=$kernel_architecture, build_id=$build_id, ImagesInternal=$ImagesInternal, build_number=$build_number, mac=$mac, board=$board, VideoInternal=$VideoInternal, AudioExternal=$AudioExternal, build_time=$build_time, wifiCount=$wifiCount, time_zone=$time_zone, release_date=$release_date, device_name=$device_name, ImagesExternal=$ImagesExternal, security_patch_level=$security_patch_level, sensorCount=$sensorCount, wifi_state=$wifi_state, gaid=$gaid, back_num=$back_num, open_time=$open_time, complete_time=$complete_time, open_power=$open_power, complete_apply_power=$complete_apply_power, telephony=$telephony, network_status=$network_status, dbm=$dbm, is_contain_sd=$is_contain_sd, front_camera_pixels=$front_camera_pixels, screen_brightness=$screen_brightness, sim_card_type=$sim_card_type, silent_mode=$silent_mode, rear_camera_pixels=$rear_camera_pixels, network_operator_country_code=$network_operator_country_code, network_operator_code=$network_operator_code, locale_country=$locale_country, locale_display_country=$locale_display_country, airplane_mode=$airplane_mode, bluetooth_flag=$bluetooth_flag, is_dualcard_standby=$is_dualcard_standby, wifi_hotspot=$wifi_hotspot, is_insert_expansion_card=$is_insert_expansion_card, version_number=$version_number, version_name=$version_name, cpu_num=$cpu_num, wifiList=$wifiList, storage=$storage, sensors=$sensors, wifiInfos=$wifiInfos, general_data=$general_data, hardware=$hardware, public_ip=$public_ip, battery_status=$battery_status, device_info=$device_info)"
    }


}