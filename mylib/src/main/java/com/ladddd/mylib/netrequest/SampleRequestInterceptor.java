package com.ladddd.mylib.netrequest;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;
import okio.Timeout;

/**
 * Created by 陈伟达 on 2017/3/8.
 */

public class SampleRequestInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        Request.Builder builder = oldRequest.newBuilder();

        RequestBody requestBody = builder.build().body();
        //根据和服务端的约定对该请求做签名处理
        //http://www.jianshu.com/p/d47da77b6419
        if (requestBody == null || requestBody instanceof MultipartBody) {

        } else {
            //对body参数进行加密
            String bodyParamString = getBodyString(requestBody);
        }
        //
        String query = oldRequest.url().encodedQuery();
        //最终加密的请求字符串： ${url}+"?"+${query}+${bodyParamString}+${data}+${appid}
        if (query == null || query.length() == 0) {

        } else {

        }

        //app名${appName}、app版本号${appVersion}、客户端类型${clientType}、系统版本${osVersion}的收集和拼装
        //获取当前 UTC时间 ${date}
        builder.addHeader("Signature", "${signature}");

        return chain.proceed(builder.build());
    }

    //将body转化为字符串
    private String getBodyString(RequestBody body) throws IOException {
        Buffer buffer = new Buffer();
        BufferedSink sink = Okio.buffer(new BufferSink(buffer));
        body.writeTo(sink);
        sink.close();

        return buffer.readUtf8();
    }

    private static class BufferSink implements Sink {

        private Buffer buffer;
        private boolean isClosed;

        public BufferSink(Buffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            if (isClosed) {

            }
            buffer.write(source, byteCount);
        }

        @Override
        public void flush() throws IOException {

        }

        @Override
        public Timeout timeout() {
            return Timeout.NONE;
        }

        @Override
        public void close() throws IOException {
            isClosed = true;
        }
    }
}
