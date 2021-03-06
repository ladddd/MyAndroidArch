package com.ladddd.myandroidarch.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.ladddd.mylib.netrequest.HttpClientProvider;

import java.io.InputStream;

/**
 * Created by 陈伟达 on 2017/6/27.
 */

public class GlideConfiguration implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        //Glide 底层默认使用 HttpConnection 进行网络请求,这里替换为 Okhttp 后才能使用本框架,进行 Glide 的加载进度监听
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(HttpClientProvider.getOkHttpClient()));
    }
}
