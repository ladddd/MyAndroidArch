package com.ladddd.myandroidarch.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.ladddd.myandroidarch.R;
import com.ladddd.mylib.BaseActivity;
import com.ladddd.mylib.utils.DimenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 陈伟达 on 2017/6/8.
 */

public class BarActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);

            try {
                ViewGroup vp = (ViewGroup) mToolbar.getParent();
                View statusBarView = new View(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        DimenUtils.getStatusBarHeight(this));
                statusBarView.setLayoutParams(params);
                statusBarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                vp.addView(statusBarView, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void initData() {

    }
}
