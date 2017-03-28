package com.ladddd.myandroidarch;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.ladddd.myandroidarch.api.GankApi;
import com.ladddd.myandroidarch.vo.GankMeiziInfo;
import com.ladddd.myandroidarch.vo.GankMeiziResult;
import com.ladddd.mylib.BaseActivity;
import com.ladddd.mylib.netrequest.RetrofitManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_request) Button btnRequest;
    @OnClick(R.id.btn_request) void request() {
        //Test
        RetrofitManager.getRetrofit(GankApi.BASE_URL)
                .create(GankApi.class)
                .getGankMeizi(20, 1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<GankMeiziResult>bindToLifecycle())
                .filter(new Predicate<GankMeiziResult>() {
                    @Override
                    public boolean test(GankMeiziResult gankMeiziResult) throws Exception {
                        return gankMeiziResult.error;
                    }
                })
                .map(new Function<GankMeiziResult, List<GankMeiziInfo>>() {
                    @Override
                    public List<GankMeiziInfo> apply(GankMeiziResult gankMeiziResult) throws Exception {
                        return gankMeiziResult.gankMeizis;
                    }
                })
                .subscribe(new Consumer<List<GankMeiziInfo>>() {
                    @Override
                    public void accept(List<GankMeiziInfo> gankMeiziInfos) throws Exception {
                        Log.d("gank", gankMeiziInfos.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("gank", "error");
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        btnRequest.setText("test");
    }
}
