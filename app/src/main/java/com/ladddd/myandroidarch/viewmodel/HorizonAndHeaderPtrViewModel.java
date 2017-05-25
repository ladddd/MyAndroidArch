package com.ladddd.myandroidarch.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.ladddd.myandroidarch.model.JianDanMeizi.JianDanMeiziData;
import com.ladddd.myandroidarch.repository.JiandanMeiziRepository;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by 陈伟达 on 2017/5/25.
 */

public class HorizonAndHeaderPtrViewModel extends ViewModel {

    private int mPage;
    private List<JianDanMeiziData> mJianDanMeiziDatas;
    private RxAppCompatActivity mActivity;
    private JiandanMeiziRepository mRepository;

    public HorizonAndHeaderPtrViewModel(JiandanMeiziRepository repository) {
        mRepository = repository;
    }

    public void init(RxAppCompatActivity activity, int page) {
        mActivity = activity;
        mPage = page;
    }

    public Observable<List<JianDanMeiziData>> getJiandanMeiziDatas() {
        mPage = 1;
        return mRepository.getJiandanMeiziDatas(mActivity, 1)
                .doOnNext(new Consumer<List<JianDanMeiziData>>() {
                    @Override
                    public void accept(List<JianDanMeiziData> jianDanMeiziDatas) throws Exception {
                        mJianDanMeiziDatas = jianDanMeiziDatas;
                    }
                });
    }

    public Observable<List<JianDanMeiziData>> getNextJiandanMeiziDatas() {
        return mRepository.getJiandanMeiziDatas(mActivity, ++mPage)
                .doOnNext(new Consumer<List<JianDanMeiziData>>() {
                    @Override
                    public void accept(List<JianDanMeiziData> jianDanMeiziDatas) throws Exception {
                        mJianDanMeiziDatas = jianDanMeiziDatas;
                    }
                });
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        public Factory() {
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new HorizonAndHeaderPtrViewModel(new JiandanMeiziRepository());
        }
    }
}
