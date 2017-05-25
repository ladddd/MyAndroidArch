package com.ladddd.myandroidarch.ui.adapter;

import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ladddd.myandroidarch.R;
import com.ladddd.myandroidarch.model.ImageModule;
import com.ladddd.mylib.config.AppConfig;
import com.ladddd.mylib.widget.RatioImageView;


/**
 * Created by 陈伟达 on 2017/5/10.
 */

public class GankMeiziAdapter extends BaseQuickAdapter<ImageModule, BaseViewHolder> {

    public GankMeiziAdapter() {
        super(R.layout.item_gank_meizi);
    }

    @Override
    protected void convert(final BaseViewHolder holder, ImageModule item) {
        final int position = holder.getAdapterPosition();
        final RatioImageView ratioImageView = holder.getView(R.id.iv_content);
        if (item.getWidth() > 0 && item.getHeight() > 0) {
            //ratio only affect when roll back. we cant get image size when first load, so imageView will
            //stay its size before recycle
            ratioImageView.setRatio(item.getWidth(), item.getHeight());
        }
        Glide.with(AppConfig.getContext())
                .load(item.getUrl())
                .asBitmap()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        ImageModule module = getItem(position);
                        if (module != null) {
                            module.setWidth(resource.getWidth());
                            module.setHeight(resource.getHeight());
                        }
                        return false;
                    }
                })
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into((RatioImageView) holder.getView(R.id.iv_content));

    }
}
