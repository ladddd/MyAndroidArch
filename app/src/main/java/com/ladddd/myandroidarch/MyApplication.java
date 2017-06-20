package com.ladddd.myandroidarch;

import com.ladddd.myandroidarch.model.dao.MyObjectBox;
import com.ladddd.mylib.MyApp;

import io.objectbox.BoxStore;

/**
 * Created by 陈伟达 on 2017/6/20.
 */

public class MyApplication extends MyApp {

    private BoxStore mBoxStore;

    @Override
    public void onCreate() {
        super.onCreate();

        mBoxStore = MyObjectBox.builder().androidContext(this).build();
    }

    public BoxStore getBoxStore() {
        return mBoxStore;
    }
}
