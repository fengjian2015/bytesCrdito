package com.software.feng.bytescrdito.http.model.risk

/**
 * Time：2024/5/20
 * Author：feng
 * Description：
 */
class AuthCalllogBean : java.io.Serializable {
    //抓取时间 13位时间戳
    var create_time: Long? = 0
    var list: List<com.software.feng.bytescrdito.http.model.risk.CalllogListBean>? = null
    override fun toString(): String {
        return "AuthCalllogBean(create_time=$create_time, list=$list)"
    }
}