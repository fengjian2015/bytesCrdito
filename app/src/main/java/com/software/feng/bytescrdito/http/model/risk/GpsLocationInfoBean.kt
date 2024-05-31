package com.software.feng.bytescrdito.http.model.risk

/**
 * Time：2024/5/20
 * Author：feng
 * Description：
 */
class GpsLocationInfoBean : java.io.Serializable {
    //gps维度
    var latitude: String? = ""

    //gps经度
    var longitude: String? = ""
    override fun toString(): String {
        return "GpsLocationInfoBean(latitude=$latitude, longitude=$longitude)"
    }

}