package com.ladddd.mylib.rx;

import com.ladddd.mylib.netrequest.entity.HttpResult;
import com.ladddd.mylib.netrequest.exception.BusinessException;
import com.ladddd.mylib.netrequest.exception.ServerException;
import com.ladddd.mylib.rx.network.StatusResponse;

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
    public static <T> ObservableTransformer<Response<HttpResult<T>>, T> handleHttpResult() {
        return new ObservableTransformer<Response<HttpResult<T>>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<Response<HttpResult<T>>> upstream) {
                return upstream.map(new Function<Response<HttpResult<T>>, T>() {
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
            }
        };
    }

    public static <T> ObservableTransformer<Response<T>, StatusResponse<T>> handleResult() {
        return new ObservableTransformer<Response<T>, StatusResponse<T>>() {
            @Override
            public ObservableSource<StatusResponse<T>> apply(Observable<Response<T>> upstream) {
                return upstream.map(new Function<Response<T>, StatusResponse<T>>() {
                    @Override
                    public StatusResponse<T> apply(Response<T> tResponse) throws Exception {
                        StatusResponse<T> statusResponse = new StatusResponse<>();
                        if (!tResponse.isSuccessful() || null == tResponse.body()) {
//                            throw new ServerException(tResponse.code(), tResponse.message());
                            statusResponse.setStatus(StatusResponse.Status.NETERR);
                        } else {
                            statusResponse.setStatus(StatusResponse.Status.OK);
                            statusResponse.setResponse(tResponse.body());
                        }
                        return statusResponse;
                    }
                });
            }
        };
    }

    public static <T> ObservableTransformer<Response<T>, T> handleResponse() {
        return new ObservableTransformer<Response<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<Response<T>> upstream) {
                return upstream.map(new Function<Response<T>, T>() {
                    @Override
                    public T apply(Response<T> tResponse) throws Exception {
                        if (!tResponse.isSuccessful() || null == tResponse.body()) {

                        }
                        return tResponse.body();
                    }
                });
            }
        };
    }
}
