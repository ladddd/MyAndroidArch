package com.ladddd.mylib.utils;

import java.util.List;

/**
 * Created by 陈伟达 on 2017/5/10.
 */

public class ListUtils {

    public static boolean isListHasData(List list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }
}
