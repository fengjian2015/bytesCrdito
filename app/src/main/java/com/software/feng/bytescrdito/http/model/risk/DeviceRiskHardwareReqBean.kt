package com.software.feng.bytescrdito.http.model.risk

/**
 * Time：2024/5/20
 * Author：feng
 * Description：
 */
class DeviceRiskHardwareReqBean : java.io.Serializable {
    //型号
    var device_model: String? = ""

    //设备号
    var imei: String? = ""

    //系统版本
    var sys_version: String? = ""

    //物理尺寸 540 * 960
    var screenResolution: String? = ""

    //品牌
    var manufacturerName: String? = ""

    //新增
    //屏幕分辨率-水平像素
    var screenxpx: String? = ""

    //屏幕分辨率-垂直像素
    var screenypx: String? = ""

    //是否检测到xposed框架 "true" ,"false"
    var is_xposed_or_root: String? = ""

    //获取CPU可运行最大频率
    var cpu_frequency: String? = ""

    //手机物理尺寸
    var physical_size: String? = ""

    //CPU型号
    var cpu_model: String? = ""

    /**
     * 连接到设备的键盘类型：  0-未定义的键盘，  1-无键键盘，  2-标 准外接键盘，  3-12键小键盘(2023/08/24新增)
     * * *  */
    var keyboard: String? = ""

    /**
     * 安卓暂存 google advertising id(google广告id）（Y）
     */
    var device_no: String? = ""
    override fun toString(): String {
        return "DeviceRiskHardwareReqBean(device_model=$device_model, imei=$imei, sys_version=$sys_version, screenResolution=$screenResolution, manufacturerName=$manufacturerName, screenxpx=$screenxpx, screenypx=$screenypx, is_xposed_or_root=$is_xposed_or_root, cpu_frequency=$cpu_frequency, physical_size=$physical_size, cpu_model=$cpu_model, keyboard=$keyboard, device_no=$device_no)"
    }


}