package com.ladddd.myandroidarch.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ladddd.myandroidarch.R;
import com.ladddd.myandroidarch.model.JianDanMeizi;
import com.ladddd.myandroidarch.ui.adapter.TestListAdapter;
import com.ladddd.myandroidarch.viewmodel.HorizonAndHeaderPtrViewModel;
import com.ladddd.mylib.BaseActivity;
import com.ladddd.mylib.netrequest.consumer.ExceptionConsumer;
import com.ladddd.mylib.ptr.MyPtrFrameLayout;
import com.ladddd.mylib.utils.ListUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * Created by 陈伟达 on 2017/5/22.
 */

public class HorizonAndHeaderPtrActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptr)
    MyPtrFrameLayout ptr;

    private TestListAdapter adapter;

    private HorizonAndHeaderPtrViewModel mViewModel;

    public static void open(@NonNull Context context) {
        Intent intent = new Intent(context, HorizonAndHeaderPtrActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_ptr);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new TestListAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        HorizonAndHeaderPtrViewModel.Factory factory = new HorizonAndHeaderPtrViewModel.Factory();
        mViewModel = ViewModelProviders.of(this, factory).get(HorizonAndHeaderPtrViewModel.class);
        mViewModel.init(this, 1);

        mViewModel.getJiandanMeiziDatas()
                .subscribe(new Consumer<List<JianDanMeizi.JianDanMeiziData>>() {
                    @Override
                    public void accept(List<JianDanMeizi.JianDanMeiziData> jianDanMeiziDatas) throws Exception {
                        if (ListUtils.isListHasData(jianDanMeiziDatas)) {
                            adapter.setNewData(jianDanMeiziDatas);
                        }
                    }
                }, getErrConsumer());
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
