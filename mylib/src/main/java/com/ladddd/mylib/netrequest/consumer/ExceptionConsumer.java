package com.ladddd.mylib.netrequest.consumer;

import com.ladddd.mylib.netrequest.exception.BusinessException;
import com.ladddd.mylib.netrequest.exception.ServerException;

import io.reactivex.functions.Consumer;

/**
 * Created by 陈伟达 on 2017/3/29.
 */

public class ExceptionConsumer<T extends Throwable> implements Consumer<T> {
    @Override
    public void accept(T t) throws Exception {
        //handle server error
        if (t instanceof ServerException) {
        }
        //handle business error
        else if (t instanceof BusinessException) {

        }
    }
}
