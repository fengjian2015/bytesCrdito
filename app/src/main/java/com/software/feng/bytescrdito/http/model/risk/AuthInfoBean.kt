package com.software.feng.bytescrdito.http.model.risk

/**
 * Time：2024/5/20
 * Author：feng
 * Description：
 */
class AuthInfoBean : java.io.Serializable {
    var applist: AuthAppListBean? = null //app 安装信息
    var sms : AuthSMSListBean? = null//短信信息
    var device_info : AuthDeviceInfoBean? = null// 设备信息
    var gps : AuthGpsBean? = null //gps信息
    var calllog_info : AuthCalllogBean? = null //通话记录信息
    var calendars : AuthCalendarsBean? = null//日历
}