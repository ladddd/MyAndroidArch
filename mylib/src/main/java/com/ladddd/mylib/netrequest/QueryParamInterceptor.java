package com.ladddd.mylib.netrequest;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 陈伟达 on 2017/3/31.
 * 过滤空的query参数
 */

public class QueryParamInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request oldRequest = chain.request();
        Request.Builder builder = oldRequest.newBuilder();
        HttpUrl.Builder urlBuilder = oldRequest.url().newBuilder();

        //这里只会检查所有同名param的第一个value，为空就清空所有同名param
        for (String param : oldRequest.url().queryParameterNames()) {
            if (TextUtils.isEmpty(oldRequest.url().queryParameter(param))) {
                urlBuilder.removeAllQueryParameters(param);
            }
        }

        builder.url(urlBuilder.build());
        return chain.proceed(builder.build());
    }
}
