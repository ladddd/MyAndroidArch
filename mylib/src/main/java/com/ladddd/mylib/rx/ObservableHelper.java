package com.ladddd.mylib.rx;

import com.ladddd.mylib.entity.HttpResult;
import com.ladddd.mylib.netrequest.exception.BusinessException;
import com.ladddd.mylib.netrequest.exception.ServerException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import retrofit2.Response;

/**
 * Created by 陈伟达 on 2017/3/29.
 */

public class ObservableHelper {

    //handle response, if business result code is not assign success, throw exception
    public static <T> ObservableTransformer<Response<HttpResult<T>>, T> handleResult() {
        return new ObservableTransformer<Response<HttpResult<T>>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<Response<HttpResult<T>>> upstream) {
                upstream.map(new Function<Response<HttpResult<T>>, T>() {
                    @Override
                    public T apply(Response<HttpResult<T>> httpResultResponse) throws Exception {
                        if (!httpResultResponse.isSuccessful() || null == httpResultResponse.body()) {
                            throw new ServerException(httpResultResponse.code(), httpResultResponse.message());
                        }
                        if (0 != httpResultResponse.body().getCode()) { //TODO set success business code
                            throw new BusinessException(httpResultResponse.body().getCode(),
                                    httpResultResponse.body().getMessage());
                        }
                        return httpResultResponse.body().getData();
                    }
                });
                return null;
            }
        };
    }
}
