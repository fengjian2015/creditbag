package com.fly.credit.jiana.network;

import com.fly.credit.jiana.manage.UserInfoManage;

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
                    .header("Authorization", UserInfoManage.INSTANCE.getToken())
                    .header("lang", "en_US")
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
