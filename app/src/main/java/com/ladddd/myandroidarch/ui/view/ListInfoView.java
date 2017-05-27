package com.ladddd.myandroidarch.ui.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ladddd.myandroidarch.R;

/**
 * Created by 陈伟达 on 2017/5/26.
 */

public class ListInfoView extends RelativeLayout {

    public ListInfoView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.test_empty_view, this);
    }

    public void setInfo(@StringRes int infoResId, @DrawableRes int imgResId) {
        ((ImageView) findViewById(R.id.iv_info)).setImageResource(imgResId);
        ((TextView) findViewById(R.id.tv_info)).setText(infoResId);
    }
}
