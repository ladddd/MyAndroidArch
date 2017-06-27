package com.ladddd.myandroidarch.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 陈伟达 on 2017/5/16.
 */

public class ImageModule implements Parcelable {
    private String id;
    private String url;
    private int width;
    private int height;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.url);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
    }

    public ImageModule() {
    }

    protected ImageModule(Parcel in) {
        this.id = in.readString();
        this.url = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
    }

    public static final Parcelable.Creator<ImageModule> CREATOR = new Parcelable.Creator<ImageModule>() {
        @Override
        public ImageModule createFromParcel(Parcel source) {
            return new ImageModule(source);
        }

        @Override
        public ImageModule[] newArray(int size) {
            return new ImageModule[size];
        }
    };
}
