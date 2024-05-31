package com.software.feng.bytescrdito.http

import com.safframework.http.interceptor.AndroidLoggingInterceptor
import com.software.feng.bytescrdito.BuildConfig
import com.software.feng.bytescrdito.common.Cons
import com.software.feng.utillibrary.http.EncryptIntercept
import com.software.feng.utillibrary.http.HttpLoggingInterceptor
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException
import java.util.concurrent.TimeUnit
import java.util.logging.Level


/**
 * Time：2024/5/12
 * Author：feng
 * Description：
 */
object NetService {

    private var newService: UserService?= null

    fun getNewService(): UserService {
        if (newService == null) {
            newService = initHttp()
        }
        return newService as UserService
    }

    private fun initHttp(): UserService{
        val builder = OkHttpClient.Builder()
        builder.writeTimeout(30 * 1000, TimeUnit.MILLISECONDS);
        builder.readTimeout(20 * 1000, TimeUnit.MILLISECONDS);
        builder.connectTimeout(15 * 1000, TimeUnit.MILLISECONDS);
        builder.sslSocketFactory(SSLUtils.getSSLSocketFactory())

//        builder.addInterceptor(AndroidLoggingInterceptor.build(hideVerticalLine=true));
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor("NetClientLog")
            loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY)
            loggingInterceptor.setColorLevel(Level.INFO)
            builder.addInterceptor(loggingInterceptor)
        }
        builder.addInterceptor(ValueInterceptor())
        builder.addInterceptor(EncryptIntercept())
        val build: Retrofit = Retrofit.Builder()
            .baseUrl(Cons.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(builder.build())
            .build()
        return build.create(UserService::class.java)
    }

    fun initUploadOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.retryOnConnectionFailure(false)
        builder.connectTimeout(60, TimeUnit.SECONDS)
        builder.readTimeout(60, TimeUnit.SECONDS)
        builder.writeTimeout(60, TimeUnit.SECONDS)
        builder.sslSocketFactory(SSLUtils.getSSLSocketFactory())
        builder.addInterceptor(ValueInterceptor())
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor("NetClientLog")
            loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY)
            loggingInterceptor.setColorLevel(Level.INFO)
            builder.addInterceptor(loggingInterceptor)
        }
        return builder.build()
    }

    fun okHttpUploadImage(file: File, callback: Callback) {
        // 创建 OkHttpClient
        val client: OkHttpClient = initUploadOkHttpClient()
        // 要上传的文件
        // 把文件封装进请求体
        val fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        // MultipartBody 上传文件专用的请求体
        val body: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM) // 表单类型(必填)
            .addFormDataPart("file", file.name, fileBody)
            .addFormDataPart("type", "jpg")
            .build()
        val request: Request = Request.Builder()
            .url(Cons.baseUrl + Cons.UPLOADIMAGE)
            .post(body)
            .build()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(call, e)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                callback.onResponse(call, response)
            }
        })
    }

    fun getResponseBody(response: Response): String {
        val UTF8 = Charset.forName("UTF-8")
        val responseBody = response.body()
        val source = responseBody!!.source()
        try {
            source.request(Long.MAX_VALUE) // Buffer the entire body.
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val buffer = source.buffer()
        var charset = UTF8
        val contentType = responseBody.contentType()
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF8)
            } catch (e: UnsupportedCharsetException) {
                e.printStackTrace()
            }
        }
        return buffer.clone().readString(charset)
    }
}