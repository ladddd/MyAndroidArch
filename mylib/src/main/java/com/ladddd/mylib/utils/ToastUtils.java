package com.ladddd.mylib.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.ladddd.mylib.config.AppConfig;

import java.lang.ref.WeakReference;

/**
 * Created by 陈伟达 on 2017/7/11.
 */

public class ToastUtils {

    private static final int DEFAULT_TEXT_COLOR = Color.WHITE;
    private static final int DEFAULT_BG_COLOR = 0x12000000;
    private static Handler sHandler = new Handler(Looper.getMainLooper());
    private static Toast toast;
    private static WeakReference<View> viewWeakReference; //custom view weak reference
    private int bgResId = -1;
    private int textColor = DEFAULT_TEXT_COLOR;
    private int backgroundColor = DEFAULT_BG_COLOR;
    private int gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    private int xOffset = 0;
    private int yOffset = (int) (64 * AppConfig.getContext().getResources().getDisplayMetrics().density + 0.5);

    public static ToastUtils getDefault() {
        return new ToastUtils();
    }

    private ToastUtils() {

    }

    public ToastUtils setBackgroundColor(@ColorInt int color) {
        backgroundColor = color;
        return this;
    }

    public ToastUtils setTextColor(@ColorInt int color) {
        textColor = color;
        return this;
    }

    public ToastUtils setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public ToastUtils setXOffset(int xOffset) {
        this.xOffset = xOffset;
        return this;
    }

    public ToastUtils setYOffset(int yOffset) {
        this.yOffset = yOffset;
        return this;
    }

    public ToastUtils setBgResId(@DrawableRes int resId) {
        this.bgResId = resId;
        return this;
    }

    public ToastUtils setView(@LayoutRes int resId) {
        View view = LayoutInflater.from(AppConfig.getContext()).inflate(resId, null);
        viewWeakReference = new WeakReference<>(view);
        return this;
    }

    public ToastUtils setView(View view) {
        viewWeakReference = new WeakReference<>(view);
        return this;
    }

    public void showShort(@StringRes final int resId) {
        show(AppConfig.getContext().getString(resId), Toast.LENGTH_SHORT);
    }

    public void showShort(@NonNull final CharSequence charSequence) {
        show(charSequence, Toast.LENGTH_SHORT);
    }

    public void showLong(@StringRes final int resId) {
        show(AppConfig.getContext().getString(resId), Toast.LENGTH_LONG);
    }

    public void showLong(@NonNull final CharSequence charSequence) {
        show(charSequence, Toast.LENGTH_LONG);
    }

    public void showShort(@StringRes int resId, Object... args) {
        show(AppConfig.getContext().getString(resId, args), Toast.LENGTH_SHORT);
    }

    public void showLong(@StringRes int resId, Object... args) {
        show(AppConfig.getContext().getString(resId, args), Toast.LENGTH_LONG);
    }

    public void show(@StringRes int resId, int duration) {
        show(AppConfig.getContext().getString(resId), duration);
    }

    public void show(@StringRes int resId, int duration, Object... args) {
        show(AppConfig.getContext().getString(resId, args), duration);
    }

    public void show(final CharSequence charSequence, final int duration) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                cancel();
                if (viewWeakReference != null && viewWeakReference.get() != null) {
                    View view = viewWeakReference.get();
                    toast = new Toast(AppConfig.getContext());
                    toast.setView(view);
                    toast.setDuration(duration);
                } else {
                    SpannableString spannableString = new SpannableString(charSequence);
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(textColor);
                    spannableString.setSpan(colorSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    toast = Toast.makeText(AppConfig.getContext(), spannableString, duration);
                    if (bgResId > 0) {
                        toast.getView().setBackgroundResource(bgResId);
                    } else if (backgroundColor != DEFAULT_BG_COLOR) {
//                        toast.getView().setBackgroundColor(backgroundColor);
                        GradientDrawable drawable = new GradientDrawable();
                        drawable.setShape(GradientDrawable.RECTANGLE);
                        drawable.setCornerRadius(DimenUtils.dip2px(AppConfig.getContext(),4));
                        drawable.setColor(backgroundColor);
//                        drawable.setAlpha(10);
                        toast.getView().setBackgroundDrawable(drawable);
                    }
                }
                toast.setGravity(gravity, xOffset, yOffset);
                toast.show();
            }
        });
    }

    public void cancel() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }
}
