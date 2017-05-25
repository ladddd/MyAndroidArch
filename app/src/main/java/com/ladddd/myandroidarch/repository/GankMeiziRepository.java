package com.ladddd.myandroidarch.repository;

import com.ladddd.myandroidarch.model.GankMeiziInfo;
import com.ladddd.myandroidarch.model.GankMeiziResult;
import com.ladddd.myandroidarch.model.ImageModule;
import com.ladddd.myandroidarch.repository.api.GankApi;
import com.ladddd.mylib.rx.RetrofitManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by 陈伟达 on 2017/5/23.
 */

public class GankMeiziRepository {

    public Observable<List<ImageModule>> getImageMoudules(RxAppCompatActivity activity, int page, int pageSize) {
        return RetrofitManager.getRetrofit(GankApi.BASE_URL)
                .create(GankApi.class)
                .getGankMeizi(pageSize, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(activity.<Response<GankMeiziResult>>bindToLifecycle())
                .map(new Function<Response<GankMeiziResult>, GankMeiziResult>() {
                    @Override
                    public GankMeiziResult apply(Response<GankMeiziResult> gankMeiziResultResponse) throws Exception {
                        return gankMeiziResultResponse.body();
                    }
                })
                .filter(new Predicate<GankMeiziResult>() {
                    @Override
                    public boolean test(GankMeiziResult gankMeiziResult) throws Exception {
                        return !gankMeiziResult.error;
                    }
                })
                .map(new Function<GankMeiziResult, List<ImageModule>>() {
                    @Override
                    public List<ImageModule> apply(GankMeiziResult gankMeiziResult) throws Exception {
                        List<ImageModule> imageModuleList = new ArrayList<>();
                        if (gankMeiziResult.gankMeizis != null) {
                            //better server bring size of image
                            for (GankMeiziInfo gankMeiziInfo : gankMeiziResult.gankMeizis) {
                                ImageModule module = new ImageModule();
                                module.setId(gankMeiziInfo._id);
                                module.setUrl(gankMeiziInfo.url);
                                module.setWidth(-1);
                                module.setHeight(-1);
                                imageModuleList.add(module);
                            }
                        }
                        return imageModuleList;
                    }
                });
    }
}
