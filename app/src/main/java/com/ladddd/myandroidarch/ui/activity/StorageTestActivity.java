package com.ladddd.myandroidarch.ui.activity;

import com.ladddd.myandroidarch.R;
import com.ladddd.mylib.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 陈伟达 on 2017/7/11.
 */

public class StorageTestActivity extends BaseActivity {



    @Override
    protected void initView() {
        setContentView(R.layout.activity_storage_test);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {

    }
}
