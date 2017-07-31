package com.ladddd.mylib;

import android.app.Application;
import android.net.NetworkInfo;

import com.ladddd.mylib.config.AppConfig;
import com.ladddd.mylib.rx.Connectivity;
import com.ladddd.mylib.rx.network.RxNetwork;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by 陈伟达 on 2017/3/6.
 */

public class MyApp extends Application {

    protected boolean isNetworkLinked;
    protected BehaviorSubject<Boolean> netStateSubject;

    public boolean isNetworkLinked() {
        return isNetworkLinked;
    }

    public BehaviorSubject<Boolean> getNetStateSubject() {
        return netStateSubject;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        netStateSubject = BehaviorSubject.create();
        RxNetwork.connectivityChanges(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Connectivity, Boolean>() {
                    @Override
                    public Boolean apply(Connectivity connectivity) throws Exception {
                        return connectivity.getState() == NetworkInfo.State.CONNECTED;
                    }
                })
                .subscribe(netStateSubject);

        init();
    }

    private void init() {
        AppConfig.init(this);
    }
}
