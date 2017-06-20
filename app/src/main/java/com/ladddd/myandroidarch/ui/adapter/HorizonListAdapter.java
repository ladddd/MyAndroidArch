package com.ladddd.myandroidarch.ui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ladddd.myandroidarch.R;
import com.ladddd.mylib.config.AppConfig;

/**
 * Created by 陈伟达 on 2017/5/31.
 */

public class HorizonListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public HorizonListAdapter() {
        super(R.layout.sub_item_horizon_list);
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {
        Glide.with(mContext.getApplicationContext())
                .load(item)
                .asBitmap()
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into((ImageView) holder.getView(R.id.iv_content));
    }
}
