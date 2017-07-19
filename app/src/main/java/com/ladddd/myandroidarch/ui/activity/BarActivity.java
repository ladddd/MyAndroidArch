package com.ladddd.myandroidarch.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bilibili.magicasakura.widgets.TintToolbar;
import com.ladddd.myandroidarch.R;
import com.ladddd.myandroidarch.ui.fragment.BarFragment;
import com.ladddd.myandroidarch.ui.view.HomeBottomSheet;
import com.ladddd.mylib.BaseActivity;
import com.ladddd.mylib.utils.DimenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 陈伟达 on 2017/6/8.
 */

public class BarActivity extends BaseActivity {

    private boolean isTransparent = false;

    @BindView(R.id.toolbar) TintToolbar mToolbar;
    @BindView(R.id.tablayout) TabLayout tablayout;
    @BindView(R.id.viewpager) ViewPager viewpager;
//    @BindView(R.id.home_bottom_sheet) HomeBottomSheet mHomeBottomSheet;

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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mDrawerToggle.syncState();

        //transparent status
        if (isTransparent && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);

            mToolbar.setPadding(0, DimenUtils.getStatusBarHeight(this), 0, 0);
        }

        //TODO more efficient fragment management
        viewpager.setOffscreenPageLimit(4);
        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return BarFragment.newInstance();
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "频道" + String.valueOf(position+1);
            }
        });

        tablayout.setupWithViewPager(viewpager);
    }

    @Override
    protected void initData() {

    }
}
