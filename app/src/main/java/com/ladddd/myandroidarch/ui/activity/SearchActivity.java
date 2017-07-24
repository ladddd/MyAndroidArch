package com.ladddd.myandroidarch.ui.activity;

import android.content.Context;
import android.content.Intent;

import com.ladddd.myandroidarch.R;
import com.ladddd.mylib.BaseActivity;

/**
 * Created by 陈伟达 on 2017/7/20.
 */

public class SearchActivity extends BaseActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void initData() {

    }
}
