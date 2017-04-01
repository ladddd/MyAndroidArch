package com.ladddd.mylib.netrequest.consumer;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ladddd.mylib.netrequest.exception.BusinessException;
import com.ladddd.mylib.netrequest.exception.ServerException;

import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

import io.reactivex.functions.Consumer;

/**
 * Created by 陈伟达 on 2017/3/29.
 */

public class ExceptionConsumer<T extends Throwable> implements Consumer<T> {

    private Consumer<? super Throwable> uiConsumer; //consumer to change user interface;

    public ExceptionConsumer(@NonNull Consumer<? super Throwable> uiConsumer) {
        this.uiConsumer = uiConsumer;
    }

    @Override
    public void accept(T t) throws Exception{
        //handle server error
        if (t instanceof SocketException ||
                t instanceof UnknownHostException ||
                t instanceof InterruptedIOException ||
                t instanceof UnknownServiceException) {
            //handle net link exception
            Log.e("gank", "net link error:" + t.getClass().getName());
        }
        else if (t instanceof ServerException) {
            //handle server exception
            Log.e("gank", "server error:" + t.getClass().getName());
        }
        //handle business error
        else if (t instanceof BusinessException) {
            //handle business exception
            Log.e("gank", "business error:" + t.getClass().getName());
        } else {
            //other exception
            Log.e("gank", "other error:" + t.getClass().getName());
        }
        uiConsumer.accept(t);
    }
}
