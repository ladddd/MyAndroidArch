package com.ladddd.mylib.utils;

import java.util.List;

/**
 * Created by 陈伟达 on 2017/5/10.
 */

public class ListUtils {

    public static boolean isListHasData(List list) {
        return list != null && list.size() > 0;
    }
}
