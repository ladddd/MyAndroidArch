package com.ladddd.myandroidarch.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.bumptech.glide.Glide;
import com.ladddd.myandroidarch.R;
import com.ladddd.myandroidarch.ui.adapter.TestAdapter;
import com.ladddd.myandroidarch.utils.ThemeHelper;
import com.ladddd.mylib.BaseActivity;
import com.ladddd.mylib.config.AppConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 陈伟达 on 2017/7/4.
 */

public class CollapseBarActivity extends BaseActivity {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_header)
    ImageView iv_header;
    @BindView(R.id.collapsing_tool_bar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @OnClick(R.id.ib_change_theme) void goToChangeTheme() {
        ThemeActivity.launch(this);
    }

    TestAdapter adapter;

    public static void launch(Context context) {
        Intent intent = new Intent(context, CollapseBarActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_collapse_bar);
        ButterKnife.bind(this);

        toolbar.setTitleTextColor(Color.WHITE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4-5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        Glide.with(AppConfig.getContext())
                .load("https://c1.hoopchina.com.cn/uploads/star/event/images/170704/bmiddle-32494065ec92312c17b894158e242d532397960e.jpg?x-oss-process=image/resize,w_800/format,webp")
                .asBitmap()
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(iv_header);

        mCollapsingToolbarLayout.setStatusBarScrimColor(ThemeUtils.getColorById(CollapseBarActivity.this, R.color.theme_color_primary_dark));
        mCollapsingToolbarLayout.setContentScrimColor(ThemeUtils.getColorById(CollapseBarActivity.this, R.color.theme_color_primary));
    }

    @Override
    protected void initData() {
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recycler.setNestedScrollingEnabled(true);

        adapter = new TestAdapter();
        recycler.setAdapter(adapter);

        Observable.create(new ObservableOnSubscribe<List<Integer>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Integer>> e) throws Exception {
                List<Integer> testList = new ArrayList<>();
                for (int i = 0; i < 100; i++) {
                    testList.add(i);
                }
                e.onNext(testList);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Integer>>() {
            @Override
            public void accept(List<Integer> integers) throws Exception {
                adapter.setNewData(integers);
            }
        });
    }

    protected void initSubscription() {

    }
}
