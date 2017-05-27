package com.ladddd.mylib.netrequest.consumer;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ladddd.mylib.utils.ListUtils;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by 陈伟达 on 2017/5/25.
 */

public class PtrConsumers<T> {

    private BaseQuickAdapter<T, BaseViewHolder> mAdapter;

    public PtrConsumers(BaseQuickAdapter<T, BaseViewHolder> adapter) {
        mAdapter = adapter;
    }

    public Consumer<List<T>> getLoadMoreSuccessConsumer() {
        return new Consumer<List<T>>() {
            @Override
            public void accept(List<T> list) throws Exception {
                if (ListUtils.isListHasData(list)) {
                    mAdapter.addData(list);
                    mAdapter.loadMoreComplete();
                } else {
                    mAdapter.loadMoreEnd();
                }
            }
        };
    }

    public Consumer<Throwable> getLoadMoreExceptionConsumer() {
        return new ExceptionConsumer<>(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mAdapter.loadMoreFail();
            }
        });
    }
}
