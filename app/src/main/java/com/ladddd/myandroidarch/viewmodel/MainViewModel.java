package com.ladddd.myandroidarch.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.ladddd.myandroidarch.model.ShareAppInfo;
import com.ladddd.myandroidarch.repository.ShareRepository;
import com.ladddd.mylib.utils.ListUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * Created by 陈伟达 on 2017/6/6.
 */

public class MainViewModel extends ViewModel {

    private RxAppCompatActivity mActivity;
    private List<ShareAppInfo> mShareAppInfos;
    private ShareRepository mShareRepository;

    public MainViewModel(ShareRepository shareRepository) {
        mShareRepository = shareRepository;
    }

    public Observable<List<ShareAppInfo>> getShareAppInfos() {
        if (ListUtils.isListHasData(mShareAppInfos)) {
            return Observable.create(new ObservableOnSubscribe<List<ShareAppInfo>>() {
                @Override
                public void subscribe(ObservableEmitter<List<ShareAppInfo>> e) throws Exception {
                    e.onNext(mShareAppInfos);
                }
            });
        }
        return mShareRepository.getShareAppInfos(mActivity)
                .doOnNext(new Consumer<List<ShareAppInfo>>() {
                    @Override
                    public void accept(List<ShareAppInfo> shareAppInfos) throws Exception {
                        mShareAppInfos = shareAppInfos;
                    }
                });
    }

    public void init(RxAppCompatActivity activity) {
        mActivity = activity;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        public Factory() {

        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new MainViewModel(new ShareRepository());
        }
    }
}
