package com.ladddd.mylib;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.ladddd.mylib.rx.enventobserve.LivingThemeUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.functions.Consumer;

/**
 * Created by 陈伟达 on 2017/3/4.
 */

public abstract class BaseActivity extends RxAppCompatActivity {

    private ThemeUtils.ExtraRefreshable mRefreshable = new ThemeUtils.ExtraRefreshable() {
        @Override
        public void refreshGlobal(Activity activity) {
            //for global setting, just do once
        }

        @Override
        public void refreshSpecificView(View view) {
            //will do this for each traversal
            colorSystemBars();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleSavedInstanceState(savedInstanceState);
        initView();
        initData();
        initSubscription();
    }

    public void colorSystemBars() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            int colorResId = getResources().getIdentifier("theme_color_primary_dark", "color", getPackageName());
            int colorValue = ThemeUtils.getColorById(BaseActivity.this, colorResId);
            getWindow().setStatusBarColor(colorValue);

            ActivityManager.TaskDescription taskDescription =
                    new ActivityManager.TaskDescription(null, null,
                            ThemeUtils.getThemeAttrColor(BaseActivity.this, android.R.attr.colorPrimary));
            setTaskDescription(taskDescription);
        }
    }

    protected abstract void initView();

    protected abstract void initData();

    protected void initSubscription() {
        LivingThemeUtils.getObservable()
                .compose(this.<Boolean>bindToLifecycle())
                .subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean initialize) throws Exception {
                if (initialize) {
                    colorSystemBars();
                } else {
                    LivingThemeUtils.refreshUiNegative(BaseActivity.this, mRefreshable);
                }
                afterThemeChanged();
            }
        });

        ((MyApp)getApplicationContext()).getNetStateSubject()
                .compose(this.<Boolean>bindToLifecycle())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        handleNetworkChanged(aBoolean);
                    }
                });
    }

    protected void afterThemeChanged() {

    }

    protected void handleSavedInstanceState(Bundle savedInstanceState) {

    }

    protected void handleNetworkChanged(boolean connected) {
        Log.d(getClass().getName(), connected?"Network Connected":"No Available Network");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
