package com.ladddd.myandroidarch.repository;

import com.ladddd.myandroidarch.MyApplication;
import com.ladddd.myandroidarch.model.GankMeiziInfo;
import com.ladddd.myandroidarch.model.GankMeiziResult;
import com.ladddd.myandroidarch.model.ImageModule;
import com.ladddd.myandroidarch.model.dao.GankMeiziDao;
import com.ladddd.myandroidarch.model.dao.GankMeiziDao_;
import com.ladddd.myandroidarch.repository.api.GankApi;
import com.ladddd.mylib.BaseActivity;
import com.ladddd.mylib.MyApp;
import com.ladddd.mylib.config.AppConfig;
import com.ladddd.mylib.rx.ObservableHelper;
import com.ladddd.mylib.rx.RetrofitManager;
import com.ladddd.mylib.rx.network.StatusResponse;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.Query;
import io.objectbox.query.QueryBuilder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by 陈伟达 on 2017/5/23.
 */

public class GankMeiziRepository {

    private Box<GankMeiziDao> gankMeiziBox;

    public GankMeiziRepository() {
        gankMeiziBox = ((MyApplication) AppConfig.getContext()).getBoxStore().boxFor(GankMeiziDao.class);
    }

    public Observable<List<ImageModule>> getImageMoudules(BaseActivity activity, final int page, int pageSize) {
        MyApp app = (MyApp) activity.getApplication();
        if (app == null || app.isNetworkLinked()) {
            return getOnlineImageMoudules(activity, page, pageSize);
        } else {
            return getOfflineImageModules(activity, page);
        }
    }

    public Observable<List<ImageModule>> getOnlineImageMoudules(BaseActivity activity, final int page, int pageSize) {
        return RetrofitManager.getRetrofit(GankApi.BASE_URL)
                .create(GankApi.class)
                .getGankMeizi(pageSize, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(activity.<Response<GankMeiziResult>>bindToLifecycle())
                .compose(ObservableHelper.<GankMeiziResult>handleResult())
                .map(new Function<StatusResponse<GankMeiziResult>, GankMeiziResult>() {
                    @Override
                    public GankMeiziResult apply(StatusResponse<GankMeiziResult> response) throws Exception {
                        GankMeiziResult gankMeiziResult = response.getResponse();
                        if (StatusResponse.Status.NETERR.equals(response.getStatus())) {
                            //offline, load from db
                            QueryBuilder<GankMeiziDao> builder = gankMeiziBox.query().equal(GankMeiziDao_.page, page);
                            Query<GankMeiziDao> query =  builder.build();
                            GankMeiziDao gankMeiziDao = query.findFirst();
                            gankMeiziResult = new GankMeiziResult(gankMeiziDao);
                        } else {
                            //update db date
                            GankMeiziDao gankMeiziDao = new GankMeiziDao(page, gankMeiziResult);
                            gankMeiziBox.put(gankMeiziDao);
                        }
                        return gankMeiziResult;
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

    public Observable<List<ImageModule>> getOfflineImageModules(BaseActivity activity, final int page) {
        return Observable.create(new ObservableOnSubscribe<GankMeiziResult>() {
            @Override
            public void subscribe(ObservableEmitter<GankMeiziResult> e) throws Exception {
                QueryBuilder<GankMeiziDao> builder = gankMeiziBox.query().equal(GankMeiziDao_.page, page);
                Query<GankMeiziDao> query =  builder.build();
                GankMeiziDao gankMeiziDao = query.findFirst();
                GankMeiziResult gankMeiziResult = new GankMeiziResult(gankMeiziDao);
                e.onNext(gankMeiziResult);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(activity.<GankMeiziResult>bindToLifecycle()).filter(new Predicate<GankMeiziResult>() {
                    @Override
                    public boolean test(GankMeiziResult gankMeiziResult) throws Exception {
                        return !gankMeiziResult.error;
                    }
                }).map(new Function<GankMeiziResult, List<ImageModule>>() {
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
