package com.ladddd.mylib;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ladddd.mylib.event.BaseEvent;
import com.ladddd.mylib.rx.Connectivity;
import com.ladddd.mylib.rx.network.RxNetwork;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 陈伟达 on 2017/3/4.
 */

public abstract class BaseActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        RxNetwork.connectivityChanges(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Connectivity, Boolean>() {
                    @Override
                    public Boolean apply(Connectivity connectivity) throws Exception {
                        return connectivity.getState() == NetworkInfo.State.CONNECTED;
                    }
                })
                .compose(this.<Boolean>bindToLifecycle())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            onNetworkConnected();
                        } else {
                            onNetworkDisconnected();
                        }
                    }
                });
        initView();
        initData();
    }

    protected abstract void initView();

    protected abstract void initData();

    protected void onNetworkConnected() {

    }

    protected void onNetworkDisconnected() {

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
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //subscribe only in main thread
    public void onMessageEvent(BaseEvent event) {

    }
}
