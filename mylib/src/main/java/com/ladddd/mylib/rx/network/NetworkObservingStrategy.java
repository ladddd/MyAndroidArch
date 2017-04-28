package com.ladddd.mylib.rx.network;

import android.content.Context;

import com.ladddd.mylib.rx.Connectivity;

import io.reactivex.Observable;

/**
 * Created by 陈伟达 on 2017/4/28.
 */

public interface NetworkObservingStrategy {

    Observable<Connectivity> getConnectivityObservable(Context context);
}
