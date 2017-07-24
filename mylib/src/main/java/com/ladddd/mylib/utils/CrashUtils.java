package com.ladddd.mylib.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.ladddd.mylib.config.AppConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 陈伟达 on 2017/7/20.
 */

public class CrashUtils {

    private static boolean mInitialized;
    private static String  versionName;
    private static int     versionCode;

    private static final Format FORMAT = new SimpleDateFormat("MM-dd HH-mm-ss", Locale.getDefault());
    private static String CRASH_HEAD;

    private static final Thread.UncaughtExceptionHandler DEFAULT_UNCAUGHT_EXCEPTION_HANDLER;
    private static final Thread.UncaughtExceptionHandler UNCAUGHT_EXCEPTION_HANDLER;

    static {
        try {
            PackageInfo pi = AppConfig.getContext().getPackageManager().getPackageInfo(AppConfig.getContext().getPackageName(), 0);
            if (pi != null) {
                versionName = pi.versionName;
                versionCode = pi.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        CRASH_HEAD = "\n************* Crash Log Head ****************" +
                "\nDevice Manufacturer: " + Build.MANUFACTURER +// 设备厂商
                "\nDevice Model       : " + Build.MODEL +// 设备型号
                "\nAndroid Version    : " + Build.VERSION.RELEASE +// 系统版本
                "\nAndroid SDK        : " + Build.VERSION.SDK_INT +// SDK版本
                "\nApp VersionName    : " + versionName +
                "\nApp VersionCode    : " + versionCode +
                "\n************* Crash Log Head ****************\n\n";

        DEFAULT_UNCAUGHT_EXCEPTION_HANDLER = Thread.getDefaultUncaughtExceptionHandler();
        UNCAUGHT_EXCEPTION_HANDLER = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, final Throwable e) {
                if (e == null) {
                    // kill app progress
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                    return;
                }
                Date now = new Date(System.currentTimeMillis());
                String fileName = FORMAT.format(now) + ".txt";
                final File logFile = new File(StorageUtils.getCrashLogDir(), fileName);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PrintWriter pw = null;
                        try {
                            pw = new PrintWriter(new FileWriter(logFile, false));
                            pw.write(CRASH_HEAD);
                            e.printStackTrace(pw);
                            Throwable cause = e.getCause();
                            while (cause != null) {
                                cause.printStackTrace(pw);
                                cause = cause.getCause();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (pw != null) {
                                pw.close();
                            }
                        }
                    }
                }).start();
                if (DEFAULT_UNCAUGHT_EXCEPTION_HANDLER != null) {
                    DEFAULT_UNCAUGHT_EXCEPTION_HANDLER.uncaughtException(t, e);
                }
            }
        };
    }

    public static void init() {
        if (mInitialized) {
            return;
        }
        File crashLogDir = StorageUtils.getCrashLogDir();
        if (crashLogDir == null || !crashLogDir.exists() || !crashLogDir.isDirectory()) {
            return;
        }
        Thread.setDefaultUncaughtExceptionHandler(UNCAUGHT_EXCEPTION_HANDLER);
        mInitialized = true;
    }
}
