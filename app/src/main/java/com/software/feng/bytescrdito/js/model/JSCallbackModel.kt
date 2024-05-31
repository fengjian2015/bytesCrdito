package com.software.feng.bytescrdito.js.model

/**
 * Time：2024/5/19
 * Author：feng
 * Description：
 */
class JSCallbackModel(var action : String = ""
                      ,var id : String = ""
                      , var status: String=""
                      ,var data : String =""
                      ,var errorMsg: String="") : java.io.Serializable {
}