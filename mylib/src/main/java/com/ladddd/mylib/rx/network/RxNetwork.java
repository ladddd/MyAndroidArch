package com.ladddd.mylib.rx.network;

import android.content.Context;
import android.os.Build;

import com.ladddd.mylib.rx.Connectivity;

import io.reactivex.Observable;

/**
 * Created by 陈伟达 on 2017/4/6.
 */

public class RxNetwork {

    public static Observable<Connectivity> connectivityChanges(Context context) {
        NetworkObservingStrategy strategy;
        if (Build.VERSION_CODES.M < Build.VERSION.SDK_INT) {
            strategy = new MarshMallowObservingStratege();
        } else if (Build.VERSION_CODES.LOLLIPOP <= Build.VERSION.SDK_INT) {
            strategy = new LolipopObservingStratege();
        } else {
            strategy = new PreLolipopObservingStrategy();
        }
        return strategy.getConnectivityObservable(context);
    }
}
