package com.software.feng.bytescrdito.http.model

/**
 * Time：2024/5/19
 * Author：feng
 * Description：
 */
class UserInfoResponse : java.io.Serializable{
    var homeUrl : String? = ""
    var token : String? = ""
    var name : String? = ""
    var phone : String? = ""
    var isNew : Boolean? = true
    var mustupdate: String? =""
    var appInstallUrl : String? = ""
}