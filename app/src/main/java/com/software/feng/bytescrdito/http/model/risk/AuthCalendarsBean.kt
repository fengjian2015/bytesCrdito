package com.software.feng.bytescrdito.http.model.risk

/**
 * Time：2024/5/20
 * Author：feng
 * Description：
 */
class AuthCalendarsBean : java.io.Serializable {
    //bigint	抓取时间 13位时间戳
    var create_time: Long? = 0

    var list: List<com.software.feng.bytescrdito.http.model.risk.CalendarsListBean>? = null
    override fun toString(): String {
        return "AuthCalendarsBean(create_time=$create_time, list=$list)"
    }

}