package com.ladddd.myandroidarch.model;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * Created by 陈伟达 on 2017/6/2.
 */

public class ShareAppInfo implements Comparable<ShareAppInfo> {
    private String packageName;
    private String activityName;
    private String label;
    private Drawable icon;
    private int priority;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(@NonNull ShareAppInfo anotherInfo) {
        return anotherInfo.priority - priority;
    }
}
