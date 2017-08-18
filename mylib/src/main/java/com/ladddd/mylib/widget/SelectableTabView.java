package com.ladddd.mylib.widget;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.graphics.drawable.DrawableCompat;
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
    protected int unreadCount;

    protected TextView tv_tab_text;
    protected ImageView iv_tab_icon;
    protected TextView tv_unread;
    protected ImageView iv_notice;

    protected int normalDrawableResId;
    protected int selectedDrawableResId;
    protected Drawable selectedDrawable;
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
        tv_unread = (TextView) contentView.findViewById(R.id.tv_unread);
        iv_notice = (ImageView) contentView.findViewById(R.id.iv_notice);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelectableTabView);
        normalDrawableResId = typedArray.getResourceId(R.styleable.SelectableTabView_drawableNormal, R.drawable.loading); //test loading res id
        selectedDrawableResId = typedArray.getResourceId(R.styleable.SelectableTabView_drawableSelected, R.drawable.loading);
        selectedDrawable = context.getResources().getDrawable(selectedDrawableResId);
        normalColor = typedArray.getColor(R.styleable.SelectableTabView_colorNormal, Color.GRAY);
        selectedColor = typedArray.getColor(R.styleable.SelectableTabView_colorSelected, Color.BLACK);
        normalTextSize = typedArray.getDimension(R.styleable.SelectableTabView_textSizeNormal, 12);
        selectedTextSize = typedArray.getDimension(R.styleable.SelectableTabView_textSizeSelected, 14);
        tabText = typedArray.getString(R.styleable.SelectableTabView_tabText);
        selected = typedArray.getBoolean(R.styleable.SelectableTabView_initialSelected, false);
        typedArray.recycle();

        //ripple effect
        if (Build.VERSION_CODES.LOLLIPOP <= Build.VERSION.SDK_INT) {
            typedArray = context.obtainStyledAttributes(new int[]{R.attr.selectableItemBackgroundBorderless});
            Drawable drawable = typedArray.getDrawable(0);
            setBackground(drawable);
        } else {
            typedArray = context.obtainStyledAttributes(new int[]{R.attr.selectableItemBackground});
            Drawable drawable = typedArray.getDrawable(0);
            setBackgroundDrawable(drawable);
        }
        typedArray.recycle();

        iv_tab_icon.setImageResource(selected?selectedDrawableResId:normalDrawableResId);
        tv_tab_text.setTextColor(selected?selectedColor:normalColor);
        tv_tab_text.setTextSize(selected?selectedTextSize:normalTextSize);
        tv_tab_text.setText(tabText);
    }

    public void setTabSelected() {
        if (selected) {
            return;
        }
        selected = true;
        iv_tab_icon.setImageDrawable(selectedDrawable);
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

    public void setUnreadCount(int count) {
        unreadCount = count;
        showUnread();
    }

    public void addUnreadCount() {
        unreadCount++;
        showUnread();
    }

    public void showNotice(boolean show) {
        iv_notice.setVisibility(show?VISIBLE:GONE);
    }

    private void showUnread() {
        if (unreadCount <= 0) {
            tv_unread.setVisibility(GONE);
        } else {
            tv_unread.setVisibility(VISIBLE);
            tv_unread.setText(unreadCount>=100?"99+":String.valueOf(unreadCount));
        }
    }

    public void tint(@ColorInt int colorValue) {
        if (iv_tab_icon != null) {
            DrawableCompat.setTint(selectedDrawable, colorValue);
            if (selected) {
                iv_tab_icon.setImageDrawable(selectedDrawable);
            }
        }
        if (tv_tab_text != null) {
            selectedColor = colorValue;
            if (selected) {
                tv_tab_text.setTextColor(selectedColor);
            }
        }
    }
}
