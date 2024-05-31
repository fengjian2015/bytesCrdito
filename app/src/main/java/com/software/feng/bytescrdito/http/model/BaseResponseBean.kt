package com.software.feng.bytescrdito.http.model

/**
 * Time：2024/5/20
 * Author：feng
 * Description：
 */
open class BaseResponseBean: java.io.Serializable {
    var code: Int = 0
    var message: String = ""
}