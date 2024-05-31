package com.software.feng.bytescrdito.http.model

/**
 * Time：2024/5/20
 * Author：feng
 * Description：
 */
class ProtocolUrlInfoResponse : java.io.Serializable {
    var protocalName :String =""//协议名称
    // 协议类型 1.隐私协议（登录页） 2.服务条款（登录页）
    var protocalType :Int =0
    var url :String =""//协议地址
}