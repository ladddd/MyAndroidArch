package com.ladddd.myandroidarch.ui.activity;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ladddd.myandroidarch.R;
import com.ladddd.myandroidarch.model.ImageModule;
import com.ladddd.myandroidarch.ui.adapter.GankMeiziAdapter;
import com.ladddd.myandroidarch.viewmodel.PtrViewModel;
import com.ladddd.mylib.BaseActivity;
import com.ladddd.mylib.netrequest.consumer.ExceptionConsumer;
import com.ladddd.mylib.netrequest.consumer.PtrConsumers;
import com.ladddd.mylib.ptr.MyPtrFrameLayout;
import com.ladddd.mylib.ptr.PtrHelper;
import com.ladddd.mylib.utils.ListUtils;
import com.ladddd.myandroidarch.ui.view.ListInfoView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * Created by 陈伟达 on 2017/4/5.
 */

public class PtrActivity extends BaseActivity {

    private GankMeiziAdapter mAdapter;
    private PtrViewModel mGankMeiziViewModel;
    private PtrConsumers<ImageModule> mLoadMoreConsumers;

    private ListInfoView mEmptyView;
    private ListInfoView mErrorView;

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
        mAdapter = new GankMeiziAdapter();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PicBrowserActivity.launch(PtrActivity.this, mGankMeiziViewModel.getImageUrls(), position);
            }
        });
        recyclerView.setAdapter(mAdapter);

        ptr.setHelper(new PtrHelper() {
            public void handleRefreshBegin() {
                mGankMeiziViewModel.getImageModules()
                        .subscribe(new Consumer<List<ImageModule>>() {
                            @Override
                            public void accept(List<ImageModule> imageModules) throws Exception {
                                ptr.refreshComplete();
                                if (!ListUtils.isListHasData(imageModules)) {
                                    //ui show no data when refresh end
                                    ptr.setRefreshResultState(MyPtrFrameLayout.STATE_LIST_EMPTY);
                                } else {
                                    //ugly fix contain same data
                                    if (imageModules.get(0).getId() != mAdapter.getData().get(0).getId()) {
                                        mAdapter.setNewData(imageModules);
                                    }

                                }
                            }
                        }, getErrConsumer());
            }

            @Override
            public void handleRefreshEnd(int stateCode) {
                if (MyPtrFrameLayout.STATE_LIST_EMPTY == stateCode) {
                    ptr.showOverlay(mEmptyView);
                } else if (MyPtrFrameLayout.STATE_NET_ERR == stateCode) {
                    ptr.showOverlay(mErrorView);
                }
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mGankMeiziViewModel.getNextImageModules()
                        .subscribe(mLoadMoreConsumers.getLoadMoreSuccessConsumer(),
                                mLoadMoreConsumers.getLoadMoreExceptionConsumer());
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
                        if (ListUtils.isListHasData(imageModules)) {
                            mAdapter.setNewData(imageModules);
                        } else {
                            ptr.showOverlay(mEmptyView);
                        }
                    }
                }, getErrConsumer());

        mLoadMoreConsumers = new PtrConsumers<>(mAdapter);
        mEmptyView = new ListInfoView(this);
        mErrorView = new ListInfoView(this);
        mErrorView.setInfo(R.string.list_err, R.mipmap.empty_icon);
    }

    private ExceptionConsumer<Throwable> getErrConsumer() {
        return new ExceptionConsumer<>(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (ptr.isRefreshing()) {
                    ptr.setRefreshResultState(MyPtrFrameLayout.STATE_NET_ERR);
                    ptr.refreshComplete();
                } else {
                    ptr.showOverlay(mErrorView);
                }
            }
        });
    }
}
