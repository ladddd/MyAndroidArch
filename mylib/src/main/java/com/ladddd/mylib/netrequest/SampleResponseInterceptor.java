package com.ladddd.mylib.netrequest;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by 陈伟达 on 2017/3/10.
 */

public class SampleResponseInterceptor implements Interceptor{

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response oldResponse = chain.proceed(request);

        //解析response，获取服务器时间
        getReponseBody(oldResponse);
        //再将服务器的时间频道request header中做另一次请求
        Request newRequest = request.newBuilder().header("Date", "${newTime}").build();
        oldResponse.body().close(); //close original response

        return chain.proceed(newRequest);
    }

    private Object getReponseBody(Response response) {
        Charset utf8 = Charset.forName("UTF-8");
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        try {
            source.request(Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Buffer buffer = source.buffer();
        long contentLength = responseBody.contentLength();

        String responseContent = "";

        if (contentLength != 0) {
            responseContent = buffer.clone().readString(utf8);
        }

        //json string to object

        return responseContent;
    }
}
