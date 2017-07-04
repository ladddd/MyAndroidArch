package com.ladddd.mylib.utils;

import android.os.Process;

import com.ladddd.mylib.config.AppConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 陈伟达 on 2017/7/4.
 * file storage manager
 *
 * inner storage & external storage
 */

public class CacheUtils {
    private static final long DEFAULT_MAX_SIZE  = Long.MAX_VALUE;
    private static final int  DEFAULT_MAX_COUNT = Integer.MAX_VALUE;

    public static final int SEC  = 1;
    public static final int MIN  = 60;
    public static final int HOUR = 3600;
    public static final int DAY  = 86400;

    private static Map<String, CacheUtils> map = new HashMap<>();
    private CacheManager cm;

    public CacheUtils getInstance() {
        return getInstance("");
    }

    public CacheUtils getInstance(String name) {
        return getInstance("", DEFAULT_MAX_SIZE, DEFAULT_MAX_COUNT);
    }

    /**
     *
     * @param name  缓存目录名
     * @param maxSize  最大缓存文件容量
     * @param maxCount 最大缓存文件个数
     * @return
     */
    public CacheUtils getInstance(String name, long maxSize, int maxCount) {
        File file = new File(AppConfig.getContext().getCacheDir(), name);
        String cacheKey = file.getAbsolutePath() + "_" + Process.myPid();
        if (map.containsKey(cacheKey)) {
            return map.get(cacheKey);
        } else {
            CacheUtils utils = new CacheUtils(file, maxSize, maxCount);
            map.put(cacheKey, utils);
            return utils;
        }
    }

    private CacheUtils(File cacheDir, long maxSize, int maxCount) {
        if (!cacheDir.exists() && !cacheDir.mkdirs()) {
            throw new RuntimeException("can't make cache dirs in" + cacheDir.getAbsolutePath());
        }
        cm = new CacheManager(cacheDir, maxSize, maxCount);
    }


    private class CacheManager {

        CacheManager(File cacheDir, long maxSize, int maxCount) {

        }
    }
}
