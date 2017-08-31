package com.ladddd.myandroidarch;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.ladddd.myandroidarch.model.dao.MyObjectBox;
import com.ladddd.myandroidarch.ui.activity.HorizonAndHeaderPtrActivity;
import com.ladddd.myandroidarch.ui.activity.MainActivity;
import com.ladddd.myandroidarch.ui.activity.PtrActivity;
import com.ladddd.myandroidarch.utils.ThemeHelper;
import com.ladddd.mylib.MyApp;
import com.ladddd.mylib.config.AppConfig;

import java.util.Arrays;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            initShortcut();
        }
    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    public void initShortcut() {
        ShortcutManager sm = getSystemService(ShortcutManager.class);
        if (sm == null) {
            return;
        }
        ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(this, "ptr")
                .setRank(1)
                .setIcon(Icon.createWithResource(this, R.mipmap.ic_test2))
                .setShortLabel(getString(R.string.ptr_shortcut_short_label))
                .setLongLabel(getString(R.string.ptr_shortcut_long_label))
                .setDisabledMessage(getString(R.string.ptr_disabled_message))
                .setIntents(new Intent[] {
                        //清空当前任务栈，重新组织
                        new Intent(this, MainActivity.class).setAction(Intent.ACTION_MAIN).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK),
                        new Intent(this, PtrActivity.class).setAction(Intent.ACTION_MAIN)
                })
                .build();
        ShortcutInfo shortcutInfo2 = new ShortcutInfo.Builder(this, "hptr")
                .setRank(0)
                .setIcon(Icon.createWithResource(this, R.mipmap.ic_test3))
                .setShortLabel(getString(R.string.hptr_shortcut_short_label))
                .setLongLabel(getString(R.string.hptr_shortcut_long_label))
                .setDisabledMessage(getString(R.string.hptr_disabled_message))
                .setIntents(new Intent[] {
                        //清空当前任务栈，重新组织
                        new Intent(this, MainActivity.class).setAction(Intent.ACTION_MAIN).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK),
                        new Intent(this, HorizonAndHeaderPtrActivity.class).setAction(Intent.ACTION_MAIN)
                })
                .build();
        sm.addDynamicShortcuts(Arrays.asList(shortcutInfo, shortcutInfo2));
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
