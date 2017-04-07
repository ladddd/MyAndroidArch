package com.ladddd.mylib.rx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * Created by 陈伟达 on 2017/4/6.
 */

public class RxNetwork {

    public static Observable<Boolean> connectivityChanges(final ConnectivityManager connectivityManager,
                                                          final Context context) {
        final IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        final BroadcastReceiver[] broadcastReceiver = new BroadcastReceiver[1];
        return Observable.create(new ObservableOnSubscribe<Intent>() {
            @Override
            public void subscribe(final ObservableEmitter<Intent> e) throws Exception {
                broadcastReceiver[0] = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        e.onNext(intent);
                    }
                };
                context.registerReceiver(broadcastReceiver[0], intentFilter);
            }
        }).map(new Function<Intent, Boolean>() {
            @Override
            public Boolean apply(Intent intent) throws Exception {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
        }).doOnDispose(new Action() {
            @Override
            public void run() throws Exception {
                try {
                    context.unregisterReceiver(broadcastReceiver[0]);
                } catch (Exception e) {
                    Log.e("RxNetwork", "broadcast receiver is already unregister");
                }
            }
        }).distinctUntilChanged();
    }

}
