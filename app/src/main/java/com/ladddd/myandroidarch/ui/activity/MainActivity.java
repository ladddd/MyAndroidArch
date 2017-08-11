package com.ladddd.myandroidarch.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ladddd.myandroidarch.R;
import com.ladddd.myandroidarch.model.ShareAppInfo;
import com.ladddd.myandroidarch.ui.adapter.ShareAdapter;
import com.ladddd.myandroidarch.viewmodel.MainViewModel;
import com.ladddd.mylib.BaseActivity;
import com.ladddd.mylib.config.AppConfig;
import com.ladddd.mylib.slidinguppanellayout.SlidingUpPanelLayout;
import com.ladddd.mylib.utils.CrashUtils;
import com.ladddd.mylib.utils.DimenUtils;
import com.ladddd.mylib.utils.SPUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


public class MainActivity extends BaseActivity {

    private MainViewModel mViewModel;
    private ShareAdapter adapter;

    private boolean slidingUpPanelPopped = false;

    @BindView(R.id.btn_ptr) Button btnGotoPtr;
    @BindView(R.id.btn_share) Button btnShowShare;
    @BindView(R.id.sliding_layout) SlidingUpPanelLayout mSlidingUpPanelLayout;
    @BindView(R.id.recycler_share) RecyclerView recyclerShare;
    @BindView(R.id.sw_night_mode)
    Switch tintSwitch;

    @OnClick(R.id.btn_ptr) void goToPtr() {
        PtrActivity.open(this);
    }

    @OnClick(R.id.btn_h_ptr) void goToHPtr() {HorizonAndHeaderPtrActivity.open(this);}

    @OnClick(R.id.btn_share) void showSharePanel() {
        if (mViewModel == null) {
            MainViewModel.Factory factory = new MainViewModel.Factory();
            mViewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);
            mViewModel.init(this);
        }
        mViewModel.getShareAppInfos().subscribe(new Consumer<List<ShareAppInfo>>() {
            @Override
            public void accept(final List<ShareAppInfo> shareAppInfos) throws Exception {
//                adapter.setNewData(shareAppInfos); insert data this place will block ui thread
                if (SlidingUpPanelLayout.PanelState.ANCHORED != mSlidingUpPanelLayout.getPanelState()) {
                    mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                    mSlidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
                        @Override
                        public void onPanelSlide(View panel, float slideOffset) {

                        }

                        @Override
                        public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                            if (SlidingUpPanelLayout.PanelState.ANCHORED.equals(newState)) {
                                adapter.setNewData(shareAppInfos);
                                recyclerShare.scrollToPosition(0);
                                mSlidingUpPanelLayout.removePanelSlideListener(this);
                            }
                        }
                    });
                } else {
                    mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
            }
        });
    }

    @OnClick(R.id.btn_bar) void goToBar() {
        BarActivity.launch(this);
    }

    @OnClick(R.id.btn_collapse_bar) void goToCollapseBar() {
        CollapseBarActivity.launch(this);
    }

    @OnClick(R.id.btn_storage) void goToStorageTest() {
        StorageTestActivity.launch(this);
    }

    @OnClick(R.id.ib_change_theme) void goToThemeChange() {
        ThemeActivity.launch(this);
    }

    @OnClick(R.id.btn_crash) void throwException() {
        Intent intent = new Intent(this, ErrorActivity.class);
        startActivity(intent);
    }

    @OnCheckedChanged(R.id.sw_night_mode) void switchNightMode(boolean checked) {
        //change theme
        toggleNightMode(checked);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recyclerShare.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));
        recyclerShare.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
                    outRect.bottom = DimenUtils.dip2px(MainActivity.this, 15);
                }
            }
        });
        adapter = new ShareAdapter();
        recyclerShare.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ShareAppInfo data = (ShareAppInfo) adapter.getItem(position);
                if (data != null && !TextUtils.isEmpty(data.getActivityName()) &&
                        !TextUtils.isEmpty(data.getPackageName())) {
                    ComponentName componentName = new ComponentName(data.getPackageName(), data.getActivityName());
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    intent.setComponent(componentName);
                    startActivity(intent);
                }
            }
        });

        tintSwitch.setChecked(SPUtils.getInstance("multiple_theme").getBoolean("is_night_mode", false));

        //request permission, generate crash log directory
        Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindToLifecycle())
                .flatMap(new Function<Long, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> apply(Long aLong) throws Exception {
                        return new RxPermissions(MainActivity.this)
                                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                }).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            CrashUtils.init();
                        }
                    }
                });
    }

    @Override
    protected void initData() {
        mSlidingUpPanelLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //hide mainActivity to background, not fully quit
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.addCategory(Intent.CATEGORY_HOME);
        startActivity(launcherIntent);
    }

    @Override
    protected void handleSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            slidingUpPanelPopped = savedInstanceState.getBoolean("isPopped", false);
        }
        if (slidingUpPanelPopped) {
            showSharePanel();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        slidingUpPanelPopped = mSlidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED ||
                mSlidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED;
        outState.putBoolean("isPopped", slidingUpPanelPopped);
    }

    private void toggleNightMode(boolean isChecked) {
        ThemeUtils.updateNightMode(getResources(), isChecked);
        SPUtils.getInstance("multiple_theme").put("is_night_mode", isChecked);
        AppConfig.resetNightMode();
        ThemeUtils.refreshUI(MainActivity.this, new ThemeUtils.ExtraRefreshable() {
                    @Override
                    public void refreshGlobal(Activity activity) {
                        //for global setting, just do once
                    }

                    @Override
                    public void refreshSpecificView(View view) {
                        //TODO: will do this for each traversal
                        if (Build.VERSION.SDK_INT >= 21) {
                            final MainActivity context = MainActivity.this;
                            ActivityManager.TaskDescription taskDescription =
                                    new ActivityManager.TaskDescription(null, null,
                                            ThemeUtils.getThemeAttrColor(context, android.R.attr.colorPrimary));
                            setTaskDescription(taskDescription);

                            getWindow().setStatusBarColor(ThemeUtils.getColorById(context, R.color.theme_color_primary_dark));
                        }
                    }
                }
        );
        //support toggle day/night will recreate the activity; but magicsakura wont
//        getDelegate().setLocalNightMode(isChecked?
//                AppCompatDelegate.MODE_NIGHT_YES
//                :AppCompatDelegate.MODE_NIGHT_NO);
    }

    protected void afterThemeChanged() {
        tintSwitch.setChecked(SPUtils.getInstance("multiple_theme").getBoolean("is_night_mode", false));
    }
}
