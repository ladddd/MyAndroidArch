package com.ladddd.myandroidarch.ui.activity;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ladddd.myandroidarch.viewmodel.PtrViewModel;
import com.ladddd.myandroidarch.R;
import com.ladddd.myandroidarch.model.ImageModule;
import com.ladddd.myandroidarch.ui.adapter.GankMeiziAdapter;
import com.ladddd.mylib.BaseActivity;
import com.ladddd.mylib.netrequest.consumer.ExceptionConsumer;
import com.ladddd.mylib.ptr.MyPtrFrameLayout;
import com.ladddd.mylib.ptr.PtrHelper;
import com.ladddd.mylib.utils.ListUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * Created by 陈伟达 on 2017/4/5.
 */

public class PtrActivity extends BaseActivity {

    private GankMeiziAdapter adapter;
    private PtrViewModel mGankMeiziViewModel;

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
                mGankMeiziViewModel.getImageModules()
                        .subscribe(new Consumer<List<ImageModule>>() {
                            @Override
                            public void accept(List<ImageModule> imageModules) throws Exception {
                                ptr.refreshComplete();
                                //ugly fix contain same data
                                Log.d("gankMeiziData", "----------real consumer----------");
                                if (ListUtils.isListHasData(imageModules) &&
                                        !imageModules.get(0).getId().equals(adapter.getData().get(0).getId())) {
                                    adapter.setNewData(imageModules);
                                }
                            }
                        }, getErrConsumer());
            }
        });

        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mGankMeiziViewModel.getNextImageModules()
                        .subscribe(new Consumer<List<ImageModule>>() {
                            @Override
                            public void accept(List<ImageModule> imageModules) throws Exception {
                                Log.d("gankMeiziData", "----------real consumer----------");
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
        PtrViewModel.Factory factory = new PtrViewModel.Factory();
        mGankMeiziViewModel = ViewModelProviders.of(this, factory).get(PtrViewModel.class);
        mGankMeiziViewModel.init(this, 1, 20);

        mGankMeiziViewModel.getImageModules()
                .subscribe(new Consumer<List<ImageModule>>() {
                    @Override
                    public void accept(List<ImageModule> imageModules) throws Exception {
                        Log.d("gankMeiziData", "----------real consumer----------");
                        adapter.setNewData(imageModules);
                    }
                }, new ExceptionConsumer<>(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //ui show no data
                    }
                }));
    }

    private ExceptionConsumer<Throwable> getErrConsumer() {
        return new ExceptionConsumer<>(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                //ui show no data

                ptr.refreshComplete();
            }
        });
    }
}
