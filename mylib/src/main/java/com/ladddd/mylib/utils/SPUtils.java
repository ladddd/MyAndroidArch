package com.ladddd.mylib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.ladddd.mylib.config.AppConfig;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by 陈伟达 on 2017/7/4.
 * sharedpreferences storage manager
 */

public class SPUtils {

    private static Map<String, SPUtils> map = new HashMap<>();
    private SharedPreferences sp;


    public static SPUtils getInstance() {
        return getInstance("");
    }


    public static SPUtils getInstance(String name) {
        if (map.containsKey(name)) {
            return map.get(name);
        } else {
            SPUtils utils = new SPUtils(name);
            map.put(name, utils);
            return utils;
        }
    }

    private SPUtils(String name) {
        sp = AppConfig.getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public void put(@NonNull String key, @NonNull final String value) {
        sp.edit().putString(key, value).apply();
    }

    public void put(@NonNull String key, final int value) {
        sp.edit().putInt(key, value).apply();
    }

    public void put(@NonNull String key, final float value) {
        sp.edit().putFloat(key, value).apply();
    }

    public void put(@NonNull String key, final long value) {
        sp.edit().putLong(key, value).apply();
    }

    public void put(@NonNull String key, final boolean value) {
        sp.edit().putBoolean(key, value).apply();
    }

    public void put(@NonNull String key, @NonNull final Set<String> value) {
        sp.edit().putStringSet(key, value).apply();
    }

    public String getString(@NonNull String key) {
        return sp.getString(key, "");
    }

    public String getString(@NonNull String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public int getInt(@NonNull String key) {
        return sp.getInt(key, 0);
    }

    public int getInt(@NonNull String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public float getFloat(@NonNull String key) {
        return sp.getFloat(key, 0);
    }

    public float getFloat(@NonNull String key, float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    public long getLong(@NonNull String key) {
        return sp.getLong(key, 0);
    }

    public long getLong(@NonNull String key, long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    public boolean getBoolean(@NonNull String key) {
        return sp.getBoolean(key, false);
    }

    public boolean getBoolean(@NonNull String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    public Set<String> getStringSet(@NonNull String key) {
        return sp.getStringSet(key, Collections.<String>emptySet());
    }

    public Set<String> getStringSet(@NonNull String key, @NonNull Set<String> defaultValue) {
        return sp.getStringSet(key, defaultValue);
    }

    public boolean contains(@NonNull String key) {
        return sp.contains(key);
    }

    public Map<String, ?> getAll() {
        return sp.getAll();
    }

    public void remove(@NonNull String key) {
        sp.edit().remove(key).apply();
    }

    public void clear() {
        sp.edit().clear().apply();
    }
}
