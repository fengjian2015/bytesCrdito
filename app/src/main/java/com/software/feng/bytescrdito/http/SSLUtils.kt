package com.software.feng.bytescrdito.http

import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Time：2024/5/12
 * Author：feng
 * Description：
 */
object SSLUtils {

    //获取这个SSLSocketFactory
    fun getSSLSocketFactory(): SSLSocketFactory? {
        return try {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(
                null,
                getTrustManager(),
                SecureRandom()
            )
            sslContext.socketFactory
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }



    //获取TrustManager
    private fun getTrustManager(): Array<TrustManager>? {
        return arrayOf(
            object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    // return null; 或者
                    return arrayOf() // 空实现
                }
            }
        )
    }
}