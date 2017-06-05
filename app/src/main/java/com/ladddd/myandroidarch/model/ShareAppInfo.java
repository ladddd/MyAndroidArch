package com.ladddd.myandroidarch.model;

import android.graphics.drawable.Drawable;

/**
 * Created by 陈伟达 on 2017/6/2.
 */

public class ShareAppInfo {
    private String activityName;
    private String label;
    private Drawable icon;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
