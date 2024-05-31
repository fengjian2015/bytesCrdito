package com.software.feng.bytescrdito.http.model.risk

/**
 * Time：2024/5/20
 * Author：feng
 * Description：
 */
class AppListRiskAppsAllReqBean : java.io.Serializable {
    //app更新时间--ms
    var last_update_time: String? = ""
    //是否系统app 1：安装包，2：系统包
    var is_system: String? = ""
    //app名
    var app_name: String? = ""
    //app安装时间--ms
    var first_install_time: String? = ""
    //是否预装 0-否 1-是
    var is_pre_install: String? = ""
    //app版本号
    var version_code: String? = ""
    //版本名
    var version_name: String? = ""
    //app package_name
    var package_name: String? = ""

    override fun toString(): String {
        return "AppListRiskAppsAllReqBean(app_name=$app_name, package_name=$package_name, version_code=$version_code, version_name=$version_name, first_install_time=$first_install_time, last_update_time=$last_update_time, is_system=$is_system, is_pre_install=$is_pre_install)"
    }
}