package com.software.feng.bytescrdito.js.model

import com.software.feng.bytescrdito.http.model.UserInfoResponse

/**
 * Time：2024/5/19
 * Author：feng
 * Description：
 */
class JSCallBackUserInfoModel : java.io.Serializable {
    var dev : String ="android"
    var version : String = ""
    var data : UserInfoResponse? =null

}