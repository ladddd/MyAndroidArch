package com.ladddd.mylib.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 陈伟达 on 2017/5/26.
 */

public class OverlayLayout extends RelativeLayout {

    protected boolean hasAttachedTarget = false;
    protected View mTargetView;

    protected Map<Integer, View> mOverlayMap;

    private OverlayLayout(Context context) {
        super(context);
    }

    public OverlayLayout(Context context, View targetView) {
        super(context);
        mOverlayMap = new HashMap<>();
        mTargetView = targetView;
    }

    private void attach() {
        if (mTargetView == null) {
            return;
        }

        ViewGroup.LayoutParams lp = mTargetView.getLayoutParams();
        setLayoutParams(lp);

        if (mTargetView.getParent() != null && mTargetView.getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) mTargetView.getParent();
            int index = parent.indexOfChild(mTargetView);
            parent.removeView(mTargetView);
            parent.addView(this, index);
            //(target_parent(target)) --> (target_parent(overlaylayout(target)))
            this.addView(mTargetView);
        }
        hasAttachedTarget = true;
    }

    public void showOverlay(View overlay) {
        if (!hasAttachedTarget) {
            attach();
        }
        if (mOverlayMap.containsKey(overlay.getId())) {
            for (Integer integer:mOverlayMap.keySet()) {
                mOverlayMap.get(integer).setVisibility(integer==overlay.getId()?VISIBLE:GONE);
            }
        } else {
            addView(overlay);
            mOverlayMap.put(overlay.getId(), overlay);
        }
    }

    public void addView(View view) {
        addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void addView(View view, int index) {
        addView(view, index, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
