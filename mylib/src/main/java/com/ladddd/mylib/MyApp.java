package com.ladddd.mylib;

import android.app.Application;
import android.net.NetworkInfo;

import com.ladddd.mylib.config.AppConfig;
import com.ladddd.mylib.event.BaseEvent;
import com.ladddd.mylib.event.EventCode;
import com.ladddd.mylib.rx.Connectivity;
import com.ladddd.mylib.rx.network.RxNetwork;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 陈伟达 on 2017/3/6.
 */

public class MyApp extends Application {

    protected boolean isNetworkLinked;

    public boolean isNetworkLinked() {
        return isNetworkLinked;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        RxNetwork.connectivityChanges(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Connectivity, Boolean>() {
                    @Override
                    public Boolean apply(Connectivity connectivity) throws Exception {
                        return connectivity.getState() == NetworkInfo.State.CONNECTED;
                    }
                })
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        notifyNetworkLinkState(aBoolean);
                    }
                });

        init();
    }

    private void init() {
        AppConfig.init(this);
    }

    private void notifyNetworkLinkState(boolean connected) {
        isNetworkLinked = connected;
//        BaseEvent<Boolean> event = new BaseEvent<>(EventCode.NET_STATE_CHANGED, connected);
//        EventBus.getDefault().post(event);
    }
}
