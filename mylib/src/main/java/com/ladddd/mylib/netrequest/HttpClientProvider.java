package com.ladddd.mylib.netrequest;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by 陈伟达 on 2017/3/7.
 */

public class HttpClientProvider {

    private static OkHttpClient mOkHttpClient;

    static {
        initOkHttpClient();
    }

    public static OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static void initOkHttpClient() {
        if (null == mOkHttpClient) {
            synchronized (HttpClientProvider.class) {
                if (null == mOkHttpClient) {
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
                            // 打印日志
                        }
                    });

                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                    //todolist-> 添加对缓存文件管理
                    Cache cache = new Cache(new File("xxx", "HttpCache"),
                            1024 * 1024 * 100);

                    mOkHttpClient = new OkHttpClient.Builder()
                            .cache(cache)
                            .addInterceptor(new SampleRequestInterceptor()) //请求签名
                            .addInterceptor(interceptor)
                            .addNetworkInterceptor(new StethoInterceptor())
                            .retryOnConnectionFailure(true)
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }
}
