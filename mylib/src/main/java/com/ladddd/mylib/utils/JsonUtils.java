package com.ladddd.mylib.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * Created by 陈伟达 on 2017/6/20.
 */

public class JsonUtils {

    private static Gson gson = new Gson();

    public static String getJsonString(Object o) {

        return gson.toJson(o);
    }

    public static <T> T jsonToObject(String jsonString, Type type) {
        T t = null;
        try {
            t = gson.fromJson(jsonString, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static <T> T jsonToObject(String jsonString, Class<T> clazzOfT) {
        T t = null;
        try {
            t = gson.fromJson(jsonString, clazzOfT);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return t;
    }
}
