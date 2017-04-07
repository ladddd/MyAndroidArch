package com.ladddd.myandroidarch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.ladddd.myandroidarch.api.GankApi;
import com.ladddd.myandroidarch.entity.GankMeiziInfo;
import com.ladddd.myandroidarch.entity.GankMeiziResult;
import com.ladddd.mylib.BaseActivity;
import com.ladddd.mylib.event.BaseEvent;
import com.ladddd.mylib.netrequest.consumer.ExceptionConsumer;
import com.ladddd.mylib.rx.RetrofitManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_request) Button btnRequest;
    @BindView(R.id.btn_ptr) Button btnGotoPtr;
    @BindView(R.id.btn_share) Button btnShowShare;

    @OnClick(R.id.btn_request) void request() {
        //Test
        RetrofitManager.getRetrofit(GankApi.BASE_URL)
                .create(GankApi.class)
                .getGankMeizi(20, 1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Response<GankMeiziResult>>bindToLifecycle())
                .map(new Function<Response<GankMeiziResult>, GankMeiziResult>() {
                    @Override
                    public GankMeiziResult apply(Response<GankMeiziResult> gankMeiziResultResponse) throws Exception {
                        return gankMeiziResultResponse.body();
                    }
                })
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
                }, new ExceptionConsumer<>(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //ui show no data
                    }
                }));
    }
    @OnClick(R.id.btn_ptr) void goToPtr() {
        PtrActivity.open(this);
    }
    @OnClick(R.id.btn_share) void showSharePanel() {
//        Intent sendIntent = new Intent();
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//        sendIntent.setType("text/plain");
//        startActivity(sendIntent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        //hide mainActivity to background, not fully quit
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.addCategory(Intent.CATEGORY_HOME);
        startActivity(launcherIntent);
    }
}
