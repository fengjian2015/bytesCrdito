package com.software.feng.utillibrary

import com.software.feng.utillibrary.util.Cons

/**
 * Time：2024/5/12
 * Author：feng
 * Description：
 */
object FengLib {

    fun setBaseUrl(url : String):FengLib{
        Cons.baseUrl = url
        return this
    }
}