package com.ladddd.myandroidarch.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.ladddd.myandroidarch.R;
import com.ladddd.myandroidarch.ui.adapter.TestAdapter;
import com.ladddd.mylib.BaseActivity;
import com.ladddd.mylib.utils.DimenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 陈伟达 on 2017/6/8.
 */

public class BarActivity extends BaseActivity {

    private boolean isTransparent = false;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.recycler_co) RecyclerView mRecyclerView;

    private TestAdapter adapter;

    public static void launch(Context context) {
        Intent intent = new Intent(context, BarActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_bar);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setSubtitle(R.string.app_name);

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,  mDrawerLayout, mToolbar,
                R.string.open, R.string.close
        );

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerToggle.syncState();

        //transparent status
        if (isTransparent && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);

            mToolbar.setPadding(0, DimenUtils.getStatusBarHeight(this), 0, 0);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setNestedScrollingEnabled(true);
    }

    @Override
    protected void initData() {
        adapter = new TestAdapter();
        mRecyclerView.setAdapter(adapter);

        Observable.create(new ObservableOnSubscribe<List<Integer>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Integer>> e) throws Exception {
                List<Integer> testList = new ArrayList<>();
                for (int i = 0; i < 100; i++) {
                    testList.add(i);
                }
                e.onNext(testList);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Integer>>() {
            @Override
            public void accept(List<Integer> integers) throws Exception {
                adapter.setNewData(integers);
            }
        });
    }
}
