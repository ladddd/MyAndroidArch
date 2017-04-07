package com.ladddd.myandroidarch;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.ladddd.mylib.BaseActivity;

/**
 * Created by 陈伟达 on 2017/4/5.
 */

public class PtrActivity extends BaseActivity {

    public static void open(@NonNull Context context) {
        Intent intent = new Intent(context, PtrActivity.class);
        context.startActivity(intent);
    }

}
