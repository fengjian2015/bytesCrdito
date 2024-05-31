package com.software.feng.utillibrary.http;

import android.text.TextUtils;


import com.software.feng.utillibrary.FengLib;
import com.software.feng.utillibrary.util.Cons;
import com.software.feng.utillibrary.util.LogUtil;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
/**
 * @author Nevio
 * on 2022/3/25
 */
public class EncryptIntercept implements Interceptor {

    private static final String METHOD_POST = "POST";
    public static final String CLOSE_SECRET = "CLOSE_SECRET";
    public static final String NET_IP = "NET_IP";


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder updateRequest = null;
        Request request = chain.request();
        if ("true".equals(request.header(NET_IP))) {
            return chain.proceed(request);
        }

        Response response = null;
        String responseDecrypt = "";
        String requestURI;

        String requestDecrypt ;
        try {
            String closeSecret = request.header(CLOSE_SECRET);
            LogUtil.INSTANCE.d( closeSecret+"");
            if ("true".equals(closeSecret)){
                response = chain.proceed(request);
            } else {
                String method = request.method();
                requestURI = request.url().encodedPath();
                updateRequest = request.newBuilder().headers(request.headers()) ;
                String baseUrl = "";
                if (METHOD_POST.equals(method)) {
                    baseUrl = Cons.INSTANCE.getBaseUrl() + requestURI;
                    String param = getRequestInfo(request);
                    if (!TextUtils.isEmpty(param)) {
                        requestDecrypt = EncryptCbcUtil.encrypt(param);
                    } else {
                        requestDecrypt = "";
                    }
                    RequestBody requestBody = RequestBody.create(request.body().contentType(), requestDecrypt);
                    updateRequest.post(requestBody);
                }
                updateRequest.url(baseUrl);
                response = chain.proceed(updateRequest.build()).newBuilder().build();
            }

            if (response.code() == 200) {
                String jsonStr = getResponseInfo(response);
                if (!TextUtils.isEmpty(jsonStr) ) {
                    if (jsonStr.contains("\"")){
                        jsonStr = jsonStr.replace("\"", "");
                    }
                    try {
                        responseDecrypt = EncryptCbcUtil.decrypt(jsonStr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    responseDecrypt = jsonStr;
                }
                //返回的
                ResponseBody responseBody = ResponseBody.create(response.body().contentType(), responseDecrypt);
                response = response.newBuilder().body(responseBody).build();
            }
            if (response == null){
                return chain.proceed(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private String getResponseInfo(Response response) {
        String str = "";
        if (response == null || !response.isSuccessful()) {
            return str;
        }
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        BufferedSource source = responseBody.source();
        try {
            source.request(Long.MAX_VALUE); // Buffer the entire body.
        } catch (IOException e) {
            e.printStackTrace();
        }
        okio.Buffer buffer = source.buffer();
        Charset charset = Charset.forName("utf-8");
        if (contentLength != 0) {
            str = buffer.clone().readString(charset);
        }
        return str;
    }

    private String getRequestInfo(Request request) {
        String str = "";
        if (request == null) {
            return str;
        }
        RequestBody requestBody = request.body();
        if (requestBody == null) {
            return str;
        }
        try {
            Buffer bufferedSink = new Buffer();
            requestBody.writeTo(bufferedSink);
            Charset charset = Charset.forName("utf-8");
            str = bufferedSink.readString(charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }






}
