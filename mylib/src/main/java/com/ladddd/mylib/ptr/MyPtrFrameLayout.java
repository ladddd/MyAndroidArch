package com.ladddd.mylib.ptr;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by 陈伟达 on 2017/3/30.
 */

public class MyPtrFrameLayout extends PtrFrameLayout {

    private PtrHelper helper;

    public void setHelper(PtrHelper ptrHelper) {
        helper = ptrHelper;
    }

    public MyPtrFrameLayout(Context context) {
        this(context, null, 0);
    }

    public MyPtrFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyPtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        disableWhenHorizontalMove(true);
        setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (helper != null) {
                    helper.handleRefreshBegin();
                }
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, content, header);
            }
        });
    }
}
