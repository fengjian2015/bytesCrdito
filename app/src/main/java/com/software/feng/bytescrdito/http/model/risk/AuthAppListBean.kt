package com.software.feng.bytescrdito.http.model.risk

/**
 * Time：2024/5/20
 * Author：feng
 * Description：
 */
class AuthAppListBean : java.io.Serializable {
    var create_time: Long  = 0
    var list: List<AppListRiskAppsAllReqBean>? = null
    override fun toString(): String {
        return "AuthAppListBean(create_time=$create_time, list=$list)"
    }
}