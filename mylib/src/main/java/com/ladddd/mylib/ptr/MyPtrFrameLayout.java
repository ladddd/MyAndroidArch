package com.ladddd.mylib.ptr;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.ladddd.mylib.widget.OverlayLayout;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by 陈伟达 on 2017/3/30.
 */

public class MyPtrFrameLayout extends PtrFrameLayout {

    public static final int STATE_LIST_EMPTY = 0x1001;
    public static final int STATE_NET_ERR = 0x1002;

    private int refreshResultState = 0;
    private static final int mLoadingMinTime = 500;
    private static final int mDurationToCloseHeader = 1000;

    private PtrHelper helper;
    private OverlayLayout mOverlayLayout;

    public void setHelper(PtrHelper ptrHelper) {
        helper = ptrHelper;
    }

    public void setRefreshResultState(int refreshResultState) {
        this.refreshResultState = refreshResultState;
    }

    public MyPtrFrameLayout(Context context) {
        super(context);
        init(context);
    }

    public MyPtrFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyPtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setLoadingMinTime(mLoadingMinTime);
        setDurationToCloseHeader(mDurationToCloseHeader);
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

        DefaultPtrHeader header = new DefaultPtrHeader(context, mLoadingMinTime);
        header.setRefreshCompleteHandler(new DefaultPtrHeader.RefreshCompleteHandler() {
            @Override
            public void onUIRefreshComplete() {
                //only call when err occasion
                if (helper != null) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            helper.handleRefreshEnd(refreshResultState);
                        }
                    }, mDurationToCloseHeader);
                }
            }
        });
        setHeaderView(header);
        addPtrUIHandler(header);

        mOverlayLayout = new OverlayLayout(context, this);
    }

    public void showOverlay(View overlay) {
        mOverlayLayout.showOverlay(overlay);
    }
}
