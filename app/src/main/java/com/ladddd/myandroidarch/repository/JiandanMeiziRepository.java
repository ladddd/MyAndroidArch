package com.ladddd.myandroidarch.repository;

import com.ladddd.myandroidarch.model.JianDanMeizi;
import com.ladddd.myandroidarch.model.JianDanMeizi.JianDanMeiziData;
import com.ladddd.myandroidarch.repository.api.JiandanApi;
import com.ladddd.mylib.rx.RetrofitManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 陈伟达 on 2017/5/25.
 */

public class JiandanMeiziRepository {

    public Observable<List<JianDanMeiziData>> getJiandanMeiziDatas(RxAppCompatActivity activity, int page) {
        return RetrofitManager.getRetrofit(JiandanApi.BASE_URL)
                .create(JiandanApi.class)
                .getJianDanMeizi(page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(activity.<JianDanMeizi>bindToLifecycle())
                .filter(new Predicate<JianDanMeizi>() {
                    @Override
                    public boolean test(JianDanMeizi jianDanMeizi) throws Exception {
                        return jianDanMeizi.comments != null;
                    }
                })
                .map(new Function<JianDanMeizi, List<JianDanMeiziData>>() {
                    @Override
                    public List<JianDanMeiziData> apply(JianDanMeizi jianDanMeizi) throws Exception {
                        return jianDanMeizi.comments;
                    }
                })
                ;

    }
}
