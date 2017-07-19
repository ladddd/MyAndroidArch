package com.ladddd.myandroidarch.ui.adapter;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ladddd.myandroidarch.R;
import com.ladddd.myandroidarch.utils.ThemeHelper;
import com.ladddd.mylib.utils.DimenUtils;

import java.util.List;

/**
 * Created by 陈伟达 on 2017/7/18.
 */

public class ThemeAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {

    public ThemeAdapter(@Nullable List<Integer> data) {
        super(R.layout.item_theme_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
        helper.setText(R.id.tv_theme_name, ThemeHelper.getName(item));
//        int colorRes = ThemeHelper.getThemeColorId(mContext, R.color.theme_color_primary, ThemeHelper.getThemeNameById(item));
//        int colorValue = mContext.getResources().getColor(colorRes);
        int colorValue =ThemeHelper.replaceColorById(mContext, item, R.color.theme_color_primary);

        Drawable drawable = helper.getView(R.id.iv_theme_color).getBackground();
        if (drawable != null && drawable instanceof GradientDrawable) {
            ((GradientDrawable)drawable).setColor(colorValue);
        } else {
            drawable = new GradientDrawable();
            ((GradientDrawable)drawable).setShape(GradientDrawable.OVAL);
            ((GradientDrawable)drawable).setColor(colorValue);
        }

        boolean isCurrentTheme = ThemeHelper.getTheme() == item;
        helper.getView(R.id.iv_theme_color).setBackgroundDrawable(drawable);
        helper.setImageDrawable(R.id.iv_theme_color,
                isCurrentTheme?mContext.getResources().getDrawable(R.drawable.ic_check_black_24dp):null);
        helper.setTextColor(R.id.tv_theme_name, colorValue);
        helper.setText(R.id.tv_theme_name, ThemeHelper.getName(item));

        helper.setText(R.id.tv_apply, isCurrentTheme?"using":"apply");
        String themeName = ThemeHelper.getThemeNameById(item);
        int themeColorId;
        if (themeName != null) {
            themeColorId = ThemeHelper.getThemeColorId(mContext, R.color.theme_color_primary, themeName);
        } else {
            themeColorId = R.color.theme_color_primary;
        }
        int btnColorValue = mContext.getResources().getColor(isCurrentTheme?themeColorId:R.color.text_light);
        helper.setTextColor(R.id.tv_apply, btnColorValue);

        Drawable applyDrawable = helper.getView(R.id.tv_apply).getBackground();
        if (applyDrawable != null && applyDrawable instanceof GradientDrawable) {
            ((GradientDrawable)applyDrawable).setStroke(DimenUtils.dip2px(mContext, 1), btnColorValue);
        } else {
            applyDrawable = new GradientDrawable();
            ((GradientDrawable)applyDrawable).setShape(GradientDrawable.RECTANGLE);
            ((GradientDrawable)applyDrawable).setStroke(DimenUtils.dip2px(mContext, 1), btnColorValue);
            ((GradientDrawable)applyDrawable).setCornerRadius(DimenUtils.dip2px(mContext, 2));
        }
        helper.getView(R.id.tv_apply).setBackgroundDrawable(applyDrawable);
    }
}
