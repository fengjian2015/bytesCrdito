package com.software.feng.bytescrdito.http.model.risk

/**
 * Time：2024/5/20
 * Author：feng
 * Description：
 */
class DeviceSensorsBean : java.io.Serializable {
    //传感器类型:1-加速度传感器 2-磁力传感器 3-方向传感器 4-陀 螺仪传感器 5-光线感应传感器 6-压力传感器 7-温度传感器
    //8-距离传感器 9-重力传感器 10-线性加速度传感器 11-旋转
    // 矢量传感器 12-湿度传感器 13-温度传感器 14-未校准磁力传
    // 感器 15-游戏动作传感器 16-未校准陀螺仪传感器 17-特殊动 作触发传感器 18-步行检测传感器 19-计步传感器
    var type: Int? = 0

    //  传感器名称
    var name: String? = null

    //解析度
    var resolution: String? = null

    //耗能
    var power: String? = null

    //最小延迟
    var min_delay: String? = null

    //最大范围
    var max_range: String? = null

    //供应商
    var vendor: String? = null

    //版本
    var version: String? = null
    override fun toString(): String {
        return "DeviceSensorsBean(type=$type, name=$name, resolution=$resolution, power=$power, min_delay=$min_delay, max_range=$max_range, vendor=$vendor, version=$version)"
    }

}