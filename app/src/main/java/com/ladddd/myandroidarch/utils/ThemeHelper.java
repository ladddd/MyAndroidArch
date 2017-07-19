package com.ladddd.myandroidarch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

import com.ladddd.myandroidarch.R;
import com.ladddd.mylib.utils.SPUtils;

/**
 * Created by 陈伟达 on 2017/7/17.
 * magic sakura demo
 */

public class ThemeHelper {
    private static final String CURRENT_THEME = "theme_current";

    public static final int CARD_SAKURA = 0x1;
    public static final int CARD_HOPE = 0x2;
    public static final int CARD_STORM = 0x3;
    public static final int CARD_WOOD = 0x4;
    public static final int CARD_LIGHT = 0x5;
    public static final int CARD_THUNDER = 0x6;
    public static final int CARD_SAND = 0x7;
    public static final int CARD_FIREY = 0x8;

    public static final Integer[] THEME_LIST = {
            CARD_SAKURA,
            CARD_HOPE,
            CARD_STORM,
            CARD_WOOD,
            CARD_LIGHT,
            CARD_THUNDER,
            CARD_SAND,
            CARD_FIREY
    };

    public static void setTheme(int themeId) {
        SPUtils.getInstance("multiple_theme").put(CURRENT_THEME, themeId);
    }

    public static int getTheme() {
        return SPUtils.getInstance("multiple_theme").getInt(CURRENT_THEME, CARD_STORM);
    }

    public static boolean isDefaultTheme() {
        return getTheme() == CARD_STORM;
    }

    public static String getName(int currentTheme) {
        switch (currentTheme) {
            case CARD_SAKURA:
                return "THE SAKURA";
            case CARD_STORM:
                return "THE STORM";
            case CARD_WOOD:
                return "THE WOOD";
            case CARD_LIGHT:
                return "THE LIGHT";
            case CARD_HOPE:
                return "THE HOPE";
            case CARD_THUNDER:
                return "THE THUNDER";
            case CARD_SAND:
                return "THE SAND";
            case CARD_FIREY:
                return "THE FIREY";
        }
        return "THE RETURN";
    }

    public static String getThemeName() {
        if (ThemeHelper.getTheme() == ThemeHelper.CARD_SAKURA) {
            return "pink";
        } else if (ThemeHelper.getTheme() == ThemeHelper.CARD_STORM) {
            return "blue";
        } else if (ThemeHelper.getTheme() == ThemeHelper.CARD_HOPE) {
            return "purple";
        } else if (ThemeHelper.getTheme() == ThemeHelper.CARD_WOOD) {
            return "green";
        } else if (ThemeHelper.getTheme() == ThemeHelper.CARD_LIGHT) {
            return "green_light";
        } else if (ThemeHelper.getTheme() == ThemeHelper.CARD_THUNDER) {
            return "yellow";
        } else if (ThemeHelper.getTheme() == ThemeHelper.CARD_SAND) {
            return "orange";
        } else if (ThemeHelper.getTheme() == ThemeHelper.CARD_FIREY) {
            return "red";
        }
        return null;
    }

    public static String getThemeNameById(int themeId) {
        if (themeId == ThemeHelper.CARD_SAKURA) {
            return "pink";
        } else if (themeId == ThemeHelper.CARD_STORM) {
            return "blue";
        } else if (themeId == ThemeHelper.CARD_HOPE) {
            return "purple";
        } else if (themeId == ThemeHelper.CARD_WOOD) {
            return "green";
        } else if (themeId == ThemeHelper.CARD_LIGHT) {
            return "green_light";
        } else if (themeId == ThemeHelper.CARD_THUNDER) {
            return "yellow";
        } else if (themeId == ThemeHelper.CARD_SAND) {
            return "orange";
        } else if (themeId == ThemeHelper.CARD_FIREY) {
            return "red";
        }
        return null;
    }

    public static @ColorRes int getThemeColorId(Context context, int colorId, String theme) {
        switch (colorId) {
            case R.color.theme_color_primary:
                return context.getResources().getIdentifier(theme, "color", context.getPackageName());
            case R.color.theme_color_primary_dark:
                return context.getResources().getIdentifier(theme + "_dark", "color", context.getPackageName());
            case R.color.theme_color_primary_trans:
                return context.getResources().getIdentifier(theme + "_trans", "color", context.getPackageName());
        }
        return colorId;
    }

    public static @ColorRes int getThemeColor(Context context, @ColorInt int color, String theme) {
        switch (color) {
            case 0xff2196F3:
                return context.getResources().getIdentifier(theme, "color", context.getPackageName());
            case 0xff1565C0:
                return context.getResources().getIdentifier(theme + "_dark", "color", context.getPackageName());
            case 0xB41A78C3:
                return context.getResources().getIdentifier(theme + "_trans", "color", context.getPackageName());
        }
        return -1;
    }

    public static int replaceColorById(Context context, int themeId, @ColorRes int colorId) {
        String themeName = ThemeHelper.getThemeNameById(themeId);
        if (themeName != null) {
            colorId = ThemeHelper.getThemeColorId(context, colorId, themeName);
        }
        return context.getResources().getColor(colorId);
    }
}
