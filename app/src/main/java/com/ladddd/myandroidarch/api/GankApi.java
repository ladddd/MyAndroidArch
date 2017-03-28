package com.ladddd.myandroidarch.api;

import com.ladddd.myandroidarch.vo.GankMeiziResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by 陈伟达 on 2017/3/24.
 */

public interface GankApi {
    String BASE_URL = "http://gank.io/api/";

    @GET("data/福利/{number}/{page}")
    Observable<GankMeiziResult> getGankMeizi(@Path("number") int number, @Path("page") int page);
}
