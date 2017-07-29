package com.ladddd.myandroidarch.ui.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.bilibili.magicasakura.widgets.TintToolbar;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ladddd.myandroidarch.R;
import com.ladddd.myandroidarch.ui.adapter.ThemeAdapter;
import com.ladddd.myandroidarch.utils.ThemeHelper;
import com.ladddd.mylib.BaseActivity;
import com.ladddd.mylib.config.AppConfig;
import com.ladddd.mylib.rx.enventobserve.LivingThemeUtils;
import com.ladddd.mylib.utils.SPUtils;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 陈伟达 on 2017/7/18.
 */

public class ThemeActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    TintToolbar mToolbar;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    public static void launch(Context context) {
        Intent intent = new Intent(context, ThemeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_theme);
        ButterKnife.bind(this);

        mToolbar.setTitle("Theme Select");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ThemeAdapter adapter = new ThemeAdapter(Arrays.asList(ThemeHelper.THEME_LIST));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position < 0 || position >= ThemeHelper.THEME_LIST.length ||
                        ThemeHelper.getTheme() == ThemeHelper.THEME_LIST[position]) {
                    return;
                }
                ThemeHelper.setTheme(ThemeHelper.THEME_LIST[position]);
                if (AppConfig.isNightMode()) {
                    ThemeUtils.updateNightMode(getResources(), false);
                    SPUtils.getInstance("multiple_theme").put("is_night_mode", false);
                    AppConfig.resetNightMode();
                }
                LivingThemeUtils.refreshUiPositive(ThemeActivity.this, new ThemeUtils.ExtraRefreshable() {
                            @Override
                            public void refreshGlobal(Activity activity) {
                                //for global setting, just do once
                                if (Build.VERSION.SDK_INT >= 21) {
                                    final ThemeActivity context = ThemeActivity.this;
                                    ActivityManager.TaskDescription taskDescription =
                                            new ActivityManager.TaskDescription(null, null,
                                                    ThemeUtils.getThemeAttrColor(context, android.R.attr.colorPrimary));
                                    setTaskDescription(taskDescription);
                                    getWindow().setStatusBarColor(
                                            ThemeUtils.getColorById(context, R.color.theme_color_primary_dark));
                                }
                            }

                            @Override
                            public void refreshSpecificView(View view) {
                                //TODO: will do this for each traversal
                            }
                        }
                );
                adapter.notifyDataSetChanged();
//                notifyThemeChanged();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {

    }
}
