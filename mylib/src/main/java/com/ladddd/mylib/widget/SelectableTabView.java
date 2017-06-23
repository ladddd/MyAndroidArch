package com.ladddd.mylib.widget;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ladddd.mylib.R;

/**
 * Created by 陈伟达 on 2017/6/23.
 */

public class SelectableTabView extends RelativeLayout {

    private static final long ANIMATION_DURATION = 300;

    protected boolean selected = false;

    protected TextView tv_tab_text;
    protected ImageView iv_tab_icon;

    protected int normalDrawableResId;
    protected int selectedDrawableResId;
    protected int normalColor;
    protected int selectedColor;
    protected float normalTextSize;
    protected float selectedTextSize;
    protected String tabText;

    public SelectableTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.selecteable_tab_view, this);
        tv_tab_text = (TextView) contentView.findViewById(R.id.tv_tab_text);
        iv_tab_icon = (ImageView) contentView.findViewById(R.id.iv_tab_icon);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelectableTabView);
        normalDrawableResId = typedArray.getResourceId(R.styleable.SelectableTabView_drawableNormal, R.drawable.loading); //test loading res id
        selectedDrawableResId = typedArray.getResourceId(R.styleable.SelectableTabView_drawableSelected, R.drawable.loading);
        normalColor = typedArray.getColor(R.styleable.SelectableTabView_colorNormal, Color.GRAY);
        selectedColor = typedArray.getColor(R.styleable.SelectableTabView_colorSelected, Color.BLACK);
        normalTextSize = typedArray.getDimension(R.styleable.SelectableTabView_textSizeNormal, 12);
        selectedTextSize = typedArray.getDimension(R.styleable.SelectableTabView_textSizeSelected, 14);
        tabText = typedArray.getString(R.styleable.SelectableTabView_tabText);
        selected = typedArray.getBoolean(R.styleable.SelectableTabView_initialSelected, false);
        typedArray.recycle();

        iv_tab_icon.setImageResource(selected?selectedDrawableResId:normalDrawableResId);
        tv_tab_text.setTextColor(selected?selectedColor:normalColor);
        tv_tab_text.setTextSize(selected?selectedTextSize:normalTextSize);
        tv_tab_text.setText(tabText);
    }

    public void toggle() {
        if (selected) {
            setTabUnSelected();
        } else {
            setTabSelected();
        }
    }

    public void setTabSelected() {
        if (selected) {
            return;
        }
        selected = true;
        iv_tab_icon.setImageResource(selectedDrawableResId);
        ObjectAnimator sizeAnimator = ObjectAnimator.ofFloat(tv_tab_text, "textSize", normalTextSize, selectedTextSize);
//        ObjectAnimator colorAnimator = ObjectAnimator.ofArgb(tv_tab_text, "textColor", normalColor, selectedColor);
        ValueAnimator colorAnimator = ValueAnimator
                .ofObject(new ArgbEvaluator(), normalColor, selectedColor);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                tv_tab_text.setTextColor((Integer) animation.getAnimatedValue());
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.play(sizeAnimator).with(colorAnimator);
        set.setDuration(ANIMATION_DURATION);
        set.start();
    }

    public void setTabUnSelected() {
        if (!selected) {
            return;
        }
        selected = false;
        iv_tab_icon.setImageResource(normalDrawableResId);
        ObjectAnimator sizeAnimator = ObjectAnimator.ofFloat(tv_tab_text, "textSize", selectedTextSize, normalTextSize);
//        ObjectAnimator colorAnimator = ObjectAnimator.ofArgb(tv_tab_text, "textColor", normalColor, selectedColor);
        ValueAnimator colorAnimator = ValueAnimator
                .ofObject(new ArgbEvaluator(), selectedColor, normalColor);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                tv_tab_text.setTextColor((Integer) animation.getAnimatedValue());
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.play(sizeAnimator).with(colorAnimator);
        set.setDuration(ANIMATION_DURATION);
        set.start();
    }
}
