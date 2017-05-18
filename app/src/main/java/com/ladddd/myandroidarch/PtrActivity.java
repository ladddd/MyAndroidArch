package com.ladddd.myandroidarch;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ladddd.myandroidarch.adapter.GankMeiziAdapter;
import com.ladddd.myandroidarch.api.GankApi;
import com.ladddd.myandroidarch.entity.GankMeiziInfo;
import com.ladddd.myandroidarch.entity.GankMeiziResult;
import com.ladddd.myandroidarch.entity.ImageModule;
import com.ladddd.mylib.BaseActivity;
import com.ladddd.mylib.netrequest.consumer.ExceptionConsumer;
import com.ladddd.mylib.ptr.MyPtrFrameLayout;
import com.ladddd.mylib.ptr.PtrHelper;
import com.ladddd.mylib.rx.RetrofitManager;
import com.ladddd.mylib.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by 陈伟达 on 2017/4/5.
 */

public class PtrActivity extends BaseActivity {

    private int page = 1;
    private GankMeiziAdapter adapter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptr)
    MyPtrFrameLayout ptr;

    public static void open(@NonNull Context context) {
        Intent intent = new Intent(context, PtrActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_ptr);
        ButterKnife.bind(this);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(null); //avoid staggeredGridLayoutManager wired animation during view convert
        adapter = new GankMeiziAdapter();
        recyclerView.setAdapter(adapter);

        ptr.setHelper(new PtrHelper() {
            @Override
            public void handleRefreshBegin() {
                page = 1;
                getGankMeiziInfo()
                        .subscribe(new Consumer<List<ImageModule>>() {
                            @Override
                            public void accept(List<ImageModule> imageModules) throws Exception {
                                ptr.refreshComplete();
                                //ugly fix contain same data
                                if (imageModules != null && imageModules.size() > 0 &&
                                        !imageModules.get(0).getId().equals(adapter.getData().get(0).getId())) {
                                    adapter.setNewData(imageModules);
                                }
                            }
                        }, new ExceptionConsumer<>(new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                //ui show no data
                                ptr.refreshComplete();
                            }
                        }));
            }
        });

        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getGankMeiziInfo()
                        .subscribe(new Consumer<List<ImageModule>>() {
                            @Override
                            public void accept(List<ImageModule> imageModules) throws Exception {
                                if (ListUtils.isListHasData(imageModules)) {
                                    adapter.addData(imageModules);
                                    adapter.loadMoreComplete();
                                } else {
                                    adapter.loadMoreEnd();
                                }
                            }
                        }, new ExceptionConsumer<>(new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                adapter.loadMoreFail();
                            }
                        }));
            }
        }, recyclerView);
    }

    @Override
    protected void initData() {
        getGankMeiziInfo()
                .subscribe(new Consumer<List<ImageModule>>() {
                    @Override
                    public void accept(List<ImageModule> imageModules) throws Exception {
                        adapter.setNewData(imageModules);
                    }
                }, new ExceptionConsumer<>(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //ui show no data
                    }
                }));
    }

    private Observable<List<ImageModule>> getGankMeiziInfo() {
        return RetrofitManager.getRetrofit(GankApi.BASE_URL)
                .create(GankApi.class)
                .getGankMeizi(20, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Response<GankMeiziResult>>bindToLifecycle())
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
