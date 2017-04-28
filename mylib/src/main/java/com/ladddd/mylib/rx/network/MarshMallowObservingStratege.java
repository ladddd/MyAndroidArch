package com.ladddd.mylib.rx.network;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import com.ladddd.mylib.rx.Connectivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Action;

/**
 * Created by 陈伟达 on 2017/4/28.
 */
@TargetApi(Build.VERSION_CODES.M)
public class MarshMallowObservingStratege implements NetworkObservingStrategy {

    private BroadcastReceiver broadcastReceiver;
    private NetworkCallback networkCallback;

    @Override
    public Observable<Connectivity> getConnectivityObservable(final Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return Observable.create(new ObservableOnSubscribe<Connectivity>() {
            @Override
            public void subscribe(final ObservableEmitter<Connectivity> e) throws Exception {
                broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        if (isIdleMode(context)) {
                            e.onNext(Connectivity.create());
                        } else {
                            e.onNext(Connectivity.create());
                        }
                    }
                };
                context.registerReceiver(broadcastReceiver, new IntentFilter(PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED));


                networkCallback = new NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        e.onNext(Connectivity.create(context));
                    }

                    @Override
                    public void onLost(Network network) {
                        e.onNext(Connectivity.create(context));
                    }
                };
                NetworkRequest networkRequest = new NetworkRequest.Builder()
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
                        .build();
                connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
            }
        }).doOnDispose(new Action() {
            @Override
            public void run() throws Exception {
                try {
                    context.unregisterReceiver(broadcastReceiver);
                } catch (Exception e) {
                    Log.e("RxNetwork", "broadcast receiver unregister exception");
                }
                try {
                    connectivityManager.unregisterNetworkCallback(networkCallback);
                } catch (Exception e) {
                    Log.e("RxNetwork", "networkCallback unregister exception");
                }
            }
        }).distinctUntilChanged();
    }

    private boolean isIdleMode(Context context) {
        String packageName = context.getPackageName();
        PowerManager manager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isIgnoringOptimizations = manager.isIgnoringBatteryOptimizations(packageName);
        return manager.isDeviceIdleMode() && !isIgnoringOptimizations;
    }
}
