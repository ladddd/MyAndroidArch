package com.ladddd.myandroidarch;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.ladddd.myandroidarch.model.dao.MyObjectBox;
import com.ladddd.myandroidarch.utils.ThemeHelper;
import com.ladddd.mylib.MyApp;
import com.ladddd.mylib.config.AppConfig;

import io.objectbox.BoxStore;

/**
 * Created by 陈伟达 on 2017/6/20.
 */

public class MyApplication extends MyApp implements ThemeUtils.switchColor {

    private BoxStore mBoxStore;

    @Override
    public void onCreate() {
        super.onCreate();
        ThemeUtils.setSwitchColor(this);
    }

    public BoxStore getBoxStore()
    {
        if (mBoxStore == null) {
            mBoxStore = MyObjectBox.builder().androidContext(this).build();
        }
        return mBoxStore;
    }

    @Override
    public int replaceColorById(Context context, @ColorRes int colorId) {
        if (colorId <= 0) {
            return context.getResources().getColor(android.R.color.transparent);
        }
        if (ThemeHelper.isDefaultTheme() || AppConfig.isNightMode()) {
            return context.getResources().getColor(colorId);
        }
        String themeName = ThemeHelper.getThemeName();
        if (themeName != null) {
            colorId = ThemeHelper.getThemeColorId(context, colorId, themeName);
        }
        return context.getResources().getColor(colorId);
    }

    @Override
    public int replaceColor(Context context, @ColorInt int originColor) {
        if (ThemeHelper.isDefaultTheme() || AppConfig.isNightMode()) {
            return originColor;
        }
        String themeName = ThemeHelper.getThemeName();
        int colorId = -1;

        if (themeName != null) {
            colorId = ThemeHelper.getThemeColor(context, originColor, themeName);
        }
        return colorId != -1 ? getResources().getColor(colorId) : originColor;
    }
}
