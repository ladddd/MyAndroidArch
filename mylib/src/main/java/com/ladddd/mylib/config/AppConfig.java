package com.ladddd.mylib.config;

import android.content.Context;

/**
 * Created by 陈伟达 on 2017/3/22.
 */

public class AppConfig {
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    @Deprecated
    public static Context getContext() {
        return mContext;
    }
}
