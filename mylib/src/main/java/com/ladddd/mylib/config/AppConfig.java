package com.ladddd.mylib.config;

import android.content.Context;

import com.ladddd.mylib.utils.SPUtils;

/**
 * Created by 陈伟达 on 2017/3/22.
 */

public class AppConfig {
    private static Context mContext;
    private static boolean isNightMode;

    public static void init(Context context) {
        mContext = context;
        isNightMode = SPUtils.getInstance("multiple_theme").getBoolean("is_night_mode", false);
    }

    public static Context getContext() {
        return mContext;
    }

    public static boolean isNightMode() {
        return isNightMode;
    }

    public static void resetNightMode() {
        isNightMode = SPUtils.getInstance("multiple_theme").getBoolean("is_night_mode", false);
    }
}
