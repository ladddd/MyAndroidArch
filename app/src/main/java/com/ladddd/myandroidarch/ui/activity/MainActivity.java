package com.ladddd.myandroidarch.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.ladddd.myandroidarch.R;
import com.ladddd.myandroidarch.model.ShareAppInfo;
import com.ladddd.myandroidarch.ui.adapter.ShareAdapter;
import com.ladddd.mylib.BaseActivity;
import com.ladddd.mylib.slidinguppanellayout.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_ptr) Button btnGotoPtr;
    @BindView(R.id.btn_share) Button btnShowShare;
    @BindView(R.id.sliding_layout) SlidingUpPanelLayout mSlidingUpPanelLayout;
    @BindView(R.id.recycler_share) RecyclerView recyclerShare;

    private ShareAdapter adapter;

    @OnClick(R.id.btn_ptr) void goToPtr() {
        PtrActivity.open(this);
    }

    @OnClick(R.id.btn_h_ptr) void goToHPtr() {HorizonAndHeaderPtrActivity.open(this);}

    @OnClick(R.id.btn_share) void showSharePanel() {
        List<ShareAppInfo> shareAppInfoList = getShareInfo();
        adapter.setNewData(shareAppInfoList);
        if (SlidingUpPanelLayout.PanelState.ANCHORED != mSlidingUpPanelLayout.getPanelState()) {
            mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        } else {
            mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recyclerShare.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));
        adapter = new ShareAdapter();
        recyclerShare.setAdapter(adapter);
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

    private List<ShareAppInfo> getShareInfo() {
        PackageManager pm = getPackageManager();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent,
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        List<ShareAppInfo> shareAppInfoList = new ArrayList<>();
        for (ResolveInfo info : activityList) {
            ShareAppInfo shareAppInfo = new ShareAppInfo();
            shareAppInfo.setActivityName(info.activityInfo.parentActivityName);
            shareAppInfo.setIcon(info.loadIcon(pm));
            shareAppInfo.setLabel(info.loadLabel(pm).toString());

            shareAppInfoList.add(shareAppInfo);
        }
        return shareAppInfoList;
    }
}
