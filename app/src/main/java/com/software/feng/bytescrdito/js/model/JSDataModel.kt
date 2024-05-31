package com.software.feng.bytescrdito.js.model

import com.google.gson.internal.LinkedTreeMap

/**
 * Time：2024/5/19
 * Author：feng
 * Description：
 */
class JSDataModel : java.io.Serializable{
    var action : String = ""
    var id : String = ""
    var data: JSDataInfo? = null
    var status : String = "0"
    var errorMsg : String = ""

    class JSDataInfo : java.io.Serializable{
        var value: String = ""
        var ip : String = ""
        var eventName : String = ""
    }
}