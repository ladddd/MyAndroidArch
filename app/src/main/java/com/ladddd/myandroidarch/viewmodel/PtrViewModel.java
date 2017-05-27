package com.ladddd.myandroidarch.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.util.Log;

import com.ladddd.myandroidarch.repository.GankMeiziRepository;
import com.ladddd.myandroidarch.model.ImageModule;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by 陈伟达 on 2017/5/23.
 */

public class PtrViewModel extends ViewModel {
    private int mPage;
    private int mPageSize;
    private List<ImageModule> mImageModules;
    private RxAppCompatActivity mActivity; //need activity dependence to handle rx lifecycle

    private GankMeiziRepository mGankMeiziRepo;

    public PtrViewModel(GankMeiziRepository gankMeiziRepo) {
        mGankMeiziRepo = gankMeiziRepo;
    }

    public void init(RxAppCompatActivity activity, int page, int pageSize) {
        mActivity = activity;
        mPage = page;
        mPageSize = pageSize;
    }

    public Observable<List<ImageModule>> getImageModules() {
        mPage = 1;
        return mGankMeiziRepo.getImageMoudules(mActivity, 1, mPageSize)
                .doOnNext(new Consumer<List<ImageModule>>() {
                    @Override
                    public void accept(List<ImageModule> imageModules) throws Exception {
                        mImageModules = imageModules;
                    }
                });
    }

    public Observable<List<ImageModule>> getNextImageModules() {
        return mGankMeiziRepo.getImageMoudules(mActivity, ++mPage, mPageSize)
                .doOnNext(new Consumer<List<ImageModule>>() {
                    @Override
                    public void accept(List<ImageModule> imageModules) throws Exception {
                        mImageModules = imageModules;
                    }
                });
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        public Factory() {

        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new PtrViewModel(new GankMeiziRepository());
        }
    }
}
