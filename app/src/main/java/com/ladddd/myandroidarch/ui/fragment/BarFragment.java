package com.ladddd.myandroidarch.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ladddd.myandroidarch.R;
import com.ladddd.myandroidarch.ui.adapter.TestAdapter;
import com.ladddd.mylib.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 陈伟达 on 2017/6/26.
 */

public class BarFragment extends BaseFragment {

    @BindView(R.id.recycler) RecyclerView recycler;
    private TestAdapter adapter;

    public static BarFragment newInstance() {
        return new BarFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_bar, new LinearLayout(getActivity()));
        ButterKnife.bind(this, contentView);

        init();

        return contentView;
    }

    private void init() {
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recycler.setNestedScrollingEnabled(true);

        adapter = new TestAdapter();
        recycler.setAdapter(adapter);

        Observable.create(new ObservableOnSubscribe<List<Integer>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Integer>> e) throws Exception {
                List<Integer> testList = new ArrayList<>();
                for (int i = 0; i < 100; i++) {
                    testList.add(i);
                }
                e.onNext(testList);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Integer>>() {
            @Override
            public void accept(List<Integer> integers) throws Exception {
                adapter.setNewData(integers);
            }
        });
    }
}
