package com.ladddd.myandroidarch.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.ladddd.myandroidarch.MyApplication;
import com.ladddd.myandroidarch.R;
import com.ladddd.myandroidarch.model.dao.TestDao;
import com.ladddd.myandroidarch.model.dao.TestDao_;
import com.ladddd.mylib.BaseActivity;
import com.ladddd.mylib.utils.CacheUtils;
import com.ladddd.mylib.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import io.objectbox.query.Query;
import io.objectbox.query.QueryBuilder;

/**
 * Created by 陈伟达 on 2017/7/11.
 */

public class StorageTestActivity extends BaseActivity {

    private Box<TestDao> testBox;

    @BindView(R.id.et_sp) EditText et_sp;
    @BindView(R.id.et_cache) EditText et_cache;
    @BindView(R.id.et_db) EditText et_db;
    @BindView(R.id.tv_value) TextView tv_value;

    @OnClick(R.id.btn_save_sp) void saveSp() {
        String newValue = et_sp.getText().toString();
        SPUtils.getInstance().put("test", newValue);
    }
    @OnClick(R.id.btn_save_cache) void saveCache() {
        String newValue = et_cache.getText().toString();
        CacheUtils.getInstance("StorageTest").put("test", newValue);
    }
    @OnClick(R.id.btn_save_db) void saveDb() {
        String newValue = et_db.getText().toString();
        TestDao testDao = new TestDao("test", newValue);
        testBox.put(testDao);
    }
    @OnClick(R.id.btn_load_sp) void loadSp() {
        String value = SPUtils.getInstance().getString("test");
        tv_value.setText(value);
    }
    @OnClick(R.id.btn_load_cache) void loadCache() {
        String value = CacheUtils.getInstance("StorageTest").getString("test");
        tv_value.setText(value);
    }
    @OnClick(R.id.btn_load_db) void loadDb() {
        QueryBuilder<TestDao> builder = testBox.query().equal(TestDao_.key, "test");
        Query<TestDao> query =  builder.build();
        TestDao testDao = query.findFirst();
        if (testDao != null) {
            String value = testDao.getValue();
            tv_value.setText(value);
        }
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, StorageTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_storage_test);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        testBox = ((MyApplication) getApplication()).getBoxStore().boxFor(TestDao.class);
    }
}
