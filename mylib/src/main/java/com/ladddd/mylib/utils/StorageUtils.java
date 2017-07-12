package com.ladddd.mylib.utils;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ladddd.mylib.config.AppConfig;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;

import io.reactivex.functions.Consumer;

/**
 * Created by 陈伟达 on 2017/7/5.
 * save media files which can be accessed from external storage
 */

public class StorageUtils {

    private static final String EXTERNAL_DIR_NAME = "nimahai";

    private static StorageUtils instance = null;

    public static StorageUtils getInstance() {
        if (instance == null) {
            instance = new StorageUtils();
        }
        return instance;
    }

    private StorageUtils() {

    }

    private File getAlbumStorageDir(String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(AppConfig.getContext().getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                //TODO
            }
        }
        return file;
    }

    private File getAlbumPublicStorageDir(String albumName) {
        File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(pictureFolder, albumName);
//        File file = new File(pictureFolder.getAbsoluteFile() + File.separator + albumName);
        if (!file.exists()) {
            if (!file.mkdir()) {
                //TODO
                Log.e("storage", "directory not generated");
            }
        }
        return file;
    }

    public void dowloadImageToLocal(Activity activity, final String imageUrl) {
        //need to check permissions dynamically, or we cant create directory under Pictures above 6.0
        new RxPermissions(activity)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Glide.with(AppConfig.getContext())
                                .load(imageUrl)
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        String fileName = EncryptUtils.encryptMD5ToString(imageUrl);
                                        File dir = getAlbumPublicStorageDir(EXTERNAL_DIR_NAME);
                                        if (!dir.exists() || !dir.isDirectory()) {
                                            //TODO err 图片目录创建失败
                                            return;
                                        }
                                        String filePath = dir.getAbsolutePath() + File.separator + fileName + ".jpg"; //affix according to Bitmap.CompressFormat.JPEG
                                        File imageFile = new File(filePath);
                                        if (!imageFile.exists()) {
                                            try {
                                                boolean ret = imageFile.createNewFile();
                                                if (!ret) {
                                                    //TODO err 图片文件创建失败
                                                    return;
                                                }
                                            } catch (IOException e) {
                                                //TODO err 图片文件创建失败
                                                return;
                                            }
                                            if (BitmapUtils.save(resource, imageFile, Bitmap.CompressFormat.JPEG, false)) {
                                                //save success
                                                ToastUtils.getDefault().showLong("已保存至" + dir.getAbsolutePath());
                                            } else {
                                                //save failed
                                            }
                                        } else {
                                            ToastUtils.getDefault().setTextColor(Color.GREEN).showShort("图片已存在");
                                        }
                                    }
                                });
                    }
                });
    }

}
