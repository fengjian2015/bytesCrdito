package com.software.feng.bytescrdito.http

import com.software.feng.bytescrdito.http.model.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


/**
 * Time：2024/5/12
 * Author：feng
 * Description：
 */
interface UserService {
    //获取短信验证码
    @POST("/app/XoHAjwZ2obcZiHaRPY8lJ+Q0CF6G4eRgKkuIUOp/nu4=")
    fun getSMS(@Body map: Map<String, String?>): Observable<SMSResponse>

    //登录
    @POST("/app/SgGwPxKCorlX/q0Hctc+ESn/aTw99Qy8aicvKNkov+w=")
    fun login(@Body map: Map<String, String?>): Observable<UserInfoResponseBase>

    //静默登录
    @POST("/app/QJ0b9mkySRzjZxNlDnuUWQigqFjnG3ISz4ENEZlJF+o=")
    fun staticLogin(@Body params: Map<String, String?>): Observable<UserInfoResponseBase>

    //退出登录
    @POST("/app/2hDNLIkuSCXGUtnsm7VwaA==")
    fun logout(@Body map: Map<String, String?>): Observable<BaseResponseBean>

    //埋点
    @POST("/app/22BZNNkzaP76ByNtjZ2BxWg0inbbBMT72r8RcDcRX/8=")
    fun addUserAction(@Body map: Map<String, String?>): Observable<BaseResponseBean>

    //提交风控信息
    @POST("/app/tgMqSVLzc9Mrpd9yOfpiJuHL/r073596XMj3OU+6U2eOUQR5owUH3+GM8m7f/q1C")
    fun uploadCreditModeLoanWardAuth(@Body map: Map<String, String?>): Observable<BaseResponseBean>

    //协议
    @POST("/app/tKmLDCZwJQfBmAYNe9+qJOC4h9TvbBA448LyePIgOuA=")
    open fun getProtocolUrlv2(@Body map: Map<String, String?>): Observable<ProtocolUrlResponse>

    //上传图片
    @POST("/app/M8OKnpOJpWn7IH7YhGU11w65EQyyMJfgbgC4Ry4pVxU=")
    open fun uploadimg(
        @Part file: MultipartBody.Part?,
        @Part type: MultipartBody.Part?
    ): Observable<UploadImgResponse?>?
}