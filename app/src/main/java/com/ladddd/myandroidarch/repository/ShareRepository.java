package com.ladddd.myandroidarch.repository;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.ladddd.myandroidarch.model.ShareAppInfo;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 陈伟达 on 2017/6/6.
 */

public class ShareRepository {

    public Observable<List<ShareAppInfo>> getShareAppInfos(final RxAppCompatActivity activity) {
        return Observable.create(new ObservableOnSubscribe<List<ShareAppInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ShareAppInfo>> e) throws Exception {
                PackageManager pm = activity.getPackageManager();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent,
                        PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
                List<ShareAppInfo> shareAppInfoList = new ArrayList<>();
                for (ResolveInfo info : activityList) {
                    ShareAppInfo shareAppInfo = new ShareAppInfo();
                    shareAppInfo.setPackageName(info.activityInfo.packageName);
                    shareAppInfo.setActivityName(info.activityInfo.name);
                    shareAppInfo.setIcon(info.loadIcon(pm));
                    shareAppInfo.setLabel(info.loadLabel(pm).toString());
                    //if from Wechat or mobileQQ, set higher priority
                    if ("com.tencent.mm".equals(info.activityInfo.packageName)) {
                        shareAppInfo.setPriority(1);
                    } else if ("com.tencent.mobileqq".equals(info.activityInfo.packageName)) {
                        shareAppInfo.setPriority(2);
                    }
                    shareAppInfoList.add(shareAppInfo);
                }
                Collections.sort(shareAppInfoList);
                e.onNext(shareAppInfoList);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(activity.<List<ShareAppInfo>>bindToLifecycle());
    }
}
