package com.ladddd.myandroidarch.ui.view.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 陈伟达 on 2017/6/22.
 * used in activity_bar.xml, control the layout scr
 */

public class BottomSheetBehavior extends CoordinatorLayout.Behavior<CardView> {

    public BottomSheetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, CardView child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, CardView child, View dependency) {
        float translationY = Math.abs(dependency.getTop());
        child.setTranslationY(translationY);
        return true;
    }
}
