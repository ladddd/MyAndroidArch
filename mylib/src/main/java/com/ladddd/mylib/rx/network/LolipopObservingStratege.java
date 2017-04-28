package com.ladddd.mylib.rx.network;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import com.ladddd.mylib.rx.Connectivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Action;

/**
 * Created by 陈伟达 on 2017/4/28.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class LolipopObservingStratege implements NetworkObservingStrategy {

    private NetworkCallback networkCallback;

    @Override
    public Observable<Connectivity> getConnectivityObservable(final Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return Observable.create(new ObservableOnSubscribe<Connectivity>() {
            @Override
            public void subscribe(final ObservableEmitter<Connectivity> e) throws Exception {
                networkCallback = new NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        e.onNext(Connectivity.create());
                    }

                    @Override
                    public void onLost(Network network) {
                        e.onNext(Connectivity.create());
                    }
                };
                connectivityManager.registerNetworkCallback(new NetworkRequest.Builder().build(), networkCallback);
            }
        }).doOnDispose(new Action() {
            @Override
            public void run() throws Exception {
                try {
                    connectivityManager.unregisterNetworkCallback(networkCallback);
                } catch (Exception e) {
                    Log.e("RxNetwork", "networkCallback unregister exception");
                }
            }
        }).distinctUntilChanged();
    }
}
