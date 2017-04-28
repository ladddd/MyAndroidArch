package com.ladddd.mylib.rx.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import com.ladddd.mylib.rx.Connectivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Action;

/**
 * Created by 陈伟达 on 2017/4/28.
 */

public class PreLolipopObservingStrategy implements NetworkObservingStrategy{

    private BroadcastReceiver broadcastReceiver;

    @Override
    public Observable<Connectivity> getConnectivityObservable(final Context context) {
        final IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        return Observable.create(new ObservableOnSubscribe<Connectivity>() {
            @Override
            public void subscribe(final ObservableEmitter<Connectivity> e) throws Exception {
                broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        e.onNext(Connectivity.create(context));
                    }
                };
                context.registerReceiver(broadcastReceiver, intentFilter);
            }
        }).doOnDispose(new Action() {
            @Override
            public void run() throws Exception {
                try {
                    context.unregisterReceiver(broadcastReceiver);
                } catch (Exception e) {
                    Log.e("RxNetwork", "broadcast receiver unregister exception");
                }
            }
        }).distinctUntilChanged();
    }
}
