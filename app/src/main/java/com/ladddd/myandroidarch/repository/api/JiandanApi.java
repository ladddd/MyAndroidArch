package com.ladddd.myandroidarch.repository.api;

import com.ladddd.myandroidarch.model.JianDanMeizi;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 陈伟达 on 2017/5/24.
 */

public interface JiandanApi {

    String BASE_URL = "http://jandan.net/";

    @GET("?oxwlxojflwblxbsapi=jandan.get_ooxx_comments")
    Observable<JianDanMeizi> getJianDanMeizi(@Query("page") int page);
}
