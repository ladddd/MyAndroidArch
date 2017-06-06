package com.ladddd.myandroidarch.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ladddd.myandroidarch.R;
import com.ladddd.myandroidarch.model.ShareAppInfo;
import com.ladddd.myandroidarch.ui.adapter.ShareAdapter;
import com.ladddd.myandroidarch.viewmodel.MainViewModel;
import com.ladddd.mylib.BaseActivity;
import com.ladddd.mylib.slidinguppanellayout.SlidingUpPanelLayout;
import com.ladddd.mylib.utils.DimenUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;


public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_ptr) Button btnGotoPtr;
    @BindView(R.id.btn_share) Button btnShowShare;
    @BindView(R.id.sliding_layout) SlidingUpPanelLayout mSlidingUpPanelLayout;
    @BindView(R.id.recycler_share) RecyclerView recyclerShare;

    private MainViewModel mViewModel;

    private ShareAdapter adapter;

    @OnClick(R.id.btn_ptr) void goToPtr() {
        PtrActivity.open(this);
    }

    @OnClick(R.id.btn_h_ptr) void goToHPtr() {HorizonAndHeaderPtrActivity.open(this);}

    @OnClick(R.id.btn_share) void showSharePanel() {
        mViewModel.getShareAppInfos().subscribe(new Consumer<List<ShareAppInfo>>() {
            @Override
            public void accept(List<ShareAppInfo> shareAppInfos) throws Exception {
                adapter.setNewData(shareAppInfos);
                if (SlidingUpPanelLayout.PanelState.ANCHORED != mSlidingUpPanelLayout.getPanelState()) {
                    mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                } else {
                    mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
            }
        });
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
    }

    @Override
    protected void initData() {
        mSlidingUpPanelLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        MainViewModel.Factory factory = new MainViewModel.Factory();
        mViewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);
        mViewModel.init(this);
    }

    @Override
    public void onBackPressed() {
        //hide mainActivity to background, not fully quit
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.addCategory(Intent.CATEGORY_HOME);
        startActivity(launcherIntent);
    }
}
