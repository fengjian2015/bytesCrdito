package com.software.feng.bytescrdito.http;


import com.software.feng.bytescrdito.js.data.JSUserInfoUtil;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ValueInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();
        if (requestBody!=null) {
            request = request.newBuilder()
                    .header("lang", "en_US")
                    .header("appName","Bytescredito")
                    .header("Authorization", JSUserInfoUtil.INSTANCE.getToken())
                    .build();
        }
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            throw e;
        }
        return response;
    }
}
