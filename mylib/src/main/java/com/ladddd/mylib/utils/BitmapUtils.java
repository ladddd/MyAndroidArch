package com.ladddd.mylib.utils;

import android.graphics.Bitmap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by 陈伟达 on 2017/7/7.
 */

public class BitmapUtils {

    private static boolean isEmptyBitmap(final Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    public static boolean save(Bitmap bitmap, File file, Bitmap.CompressFormat format, boolean recycle) {
        if (isEmptyBitmap(bitmap) || !file.exists()) {
            return false;
        }
        OutputStream os = null;
        boolean ret = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ret = bitmap.compress(format, 100, os);
            if (recycle && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return ret;
    }
}
