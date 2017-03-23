package com.ladddd.mylib;

import android.app.Application;

import com.ladddd.mylib.config.AppConfig;

/**
 * Created by 陈伟达 on 2017/3/6.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init() {
        AppConfig.init(this);
    }
}
