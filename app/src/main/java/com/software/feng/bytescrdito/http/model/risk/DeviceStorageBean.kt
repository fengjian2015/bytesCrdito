package com.software.feng.bytescrdito.http.model.risk

/**
 * Time：2024/5/20
 * Author：feng
 * Description：
 */
class DeviceStorageBean : java.io.Serializable {

    //手机内部存储可用大小
    var  availableDiskSize: String? = ""

    //手机内部存储可用大小
    var  availableMemory: String? = ""

    //从开机到现在的毫秒数(包括睡眠时间)
    var  elapsedRealtime: String? = ""

    //开机总时长 非休眠时间 13位时间戳
    var totalBootTimeWak : String? =""

    //是否开启 USB 调试
    var  isUSBDebug: String? = ""

    //是否使用代理
    var  isUsingProxyPort: String? = ""

    //是否使用VPN
    var  isUsingVPN: String? = ""

    //手机内部存储总大小
    var  memorySize: String? = ""

    //外置存储空间（存储卡）可用大小
    var memoryUsableSize: String? = ""

    //外置存储空间（存储卡）已用大小
    var memoryUseSize: String? =""

    //总内存大小
    var  ram_total_size: String? = ""

    //磁盘（内置存储）空间总大小
    var  totalDiskSize: String? = ""

    //内存使用大小
    var ramUsedSize: String? = ""

    //app最大占用内存
    var appMaxMemory : String? = ""

    //app当前可用内存
    var appAvailableMemory : String? = ""

    //app可释放内存
    var app_free_memory : String? =""
    override fun toString(): String {
        return "DeviceStorageBean(availableDiskSize=$availableDiskSize, availableMemory=$availableMemory, elapsedRealtime=$elapsedRealtime, totalBootTimeWak=$totalBootTimeWak, isUSBDebug=$isUSBDebug, isUsingProxyPort=$isUsingProxyPort, isUsingVPN=$isUsingVPN, memorySize=$memorySize, memoryUsableSize=$memoryUsableSize, memoryUseSize=$memoryUseSize, ram_total_size=$ram_total_size, totalDiskSize=$totalDiskSize, ramUsedSize=$ramUsedSize, appMaxMemory=$appMaxMemory, appAvailableMemory=$appAvailableMemory, app_free_memory=$app_free_memory)"
    }


}