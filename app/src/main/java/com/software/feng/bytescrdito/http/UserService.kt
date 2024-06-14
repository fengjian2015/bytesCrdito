package com.software.feng.bytescrdito.http

import com.software.feng.bytescrdito.http.model.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


/**
 * Time：2024/5/12
 * Author：feng
 * Description：正式环境
 */
interface UserService {
    //获取短信验证码
    @POST("/app/ggPWLzQnLgxyemRgzZjZrgg9r1fpTYHABh5kMKVJ0GE=")
    fun getSMS(@Body map: Map<String, String?>): Observable<SMSResponse>

    //登录
    @POST("/app/+cwwWjxsehKOdV/wpCkEq81FMx8Aap+R/s/khBwk9h0=")
    fun login(@Body map: Map<String, String?>): Observable<UserInfoResponseBase>

    //静默登录
    @POST("/app/92CoZtSEUdNBCBnGSkhol23OeBjGDjhkiE0lLWxxruI=")
    fun staticLogin(@Body params: Map<String, String?>): Observable<UserInfoResponseBase>

    //退出登录
    @POST("/app/no4OT2pkOnZOcL3MP1ujNw==")
    fun logout(@Body map: Map<String, String?>): Observable<BaseResponseBean>

    //埋点
    @POST("/app/U01eJb8GiIfaJFitd9eOVphDXMJWtliEj+iasw8MUwo=")
    fun addUserAction(@Body map: Map<String, String?>): Observable<BaseResponseBean>

    //提交风控信息
    @POST("/app/gZfkhJdhypv2j8glnf9yYkCxsSYc/eDtqbPNvInh2x4I7mDur6flqUnI4m0UT7xu")
    fun uploadCreditModeLoanWardAuth(@Body map: Map<String, String?>): Observable<BaseResponseBean>

    //协议
    @POST("/app/WZDamUcKUgrybN3deT1QTVBt9i5s8U8I2sxk+IjsCaU=")
    open fun getProtocolUrlv2(@Body map: Map<String, String?>): Observable<ProtocolUrlResponse>

    //上传图片
    @POST("/app/1P81UZvvnqBhaR6lqdbLhOo761XVSttI/e3HknCZyUs=")
    open fun uploadimg(
        @Part file: MultipartBody.Part?,
        @Part type: MultipartBody.Part?
    ): Observable<UploadImgResponse?>?
}