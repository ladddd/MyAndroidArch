package com.ladddd.myandroidarch;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ladddd.mylib.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 陈伟达 on 2017/4/5.
 */

public class PtrActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public static void open(@NonNull Context context) {
        Intent intent = new Intent(context, PtrActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_ptr);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    @Override
    protected void initData() {

    }
}
