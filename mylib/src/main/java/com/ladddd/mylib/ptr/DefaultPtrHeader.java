package com.ladddd.mylib.ptr;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ladddd.mylib.R;
import com.ladddd.mylib.utils.DimenUtils;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by 陈伟达 on 2017/5/17.
 */

public class DefaultPtrHeader extends FrameLayout implements PtrUIHandler {

    private Context mContext;
    private ImageView ivLoading;
    private RefreshCompleteHandler mHandler;

    private int mLoadingMinTime;
    private ObjectAnimator rotateAnimator;

    public DefaultPtrHeader(@NonNull Context context, int loadingMinTime) {
        super(context);
        initView(context, loadingMinTime);
    }

    private void initView(Context context, int loadingMinTime) {
        mContext = context;
        mLoadingMinTime = loadingMinTime;
        View contentView = LayoutInflater.from(context).inflate(R.layout.default_ptr_header, this);
        ivLoading = (ImageView) contentView.findViewById(R.id.iv_loading);

        rotateAnimator = ObjectAnimator.ofFloat(ivLoading, "rotation", 0f, 360f);
        rotateAnimator.setDuration(mLoadingMinTime);
        rotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimator.setRepeatMode(ValueAnimator.RESTART);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        rotateAnimator.start();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        rotateAnimator.end();
        //ui notice
        if (mHandler != null) {
            mHandler.onUIRefreshComplete();
        }
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        final int offsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();

        final int targetPos = DimenUtils.dip2px(mContext, 15);
        if (currentPos > targetPos) {
            ValueAnimator animator = ValueAnimator.ofInt(lastPos, currentPos);
            animator.setDuration(100);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int val = (Integer) animation.getAnimatedValue();
                    ViewGroup.LayoutParams lp = ivLoading.getLayoutParams();
                    lp.width = val;
                    lp.height = val;
                    ivLoading.setLayoutParams(lp);
                }
            });
            animator.start();
        }
    }

    public void setRefreshCompleteHandler(RefreshCompleteHandler handler) {
        mHandler = handler;
    }

    interface RefreshCompleteHandler {
        void onUIRefreshComplete();
    }
}
