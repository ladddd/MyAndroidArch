package com.ladddd.myandroidarch.ui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ladddd.myandroidarch.R;
import com.ladddd.myandroidarch.model.JianDanMeizi.JianDanMeiziData;
import com.ladddd.mylib.config.AppConfig;
import com.ladddd.mylib.utils.ListUtils;

/**
 * Created by 陈伟达 on 2017/5/22.
 */

public class TestListAdapter extends BaseQuickAdapter<JianDanMeiziData, BaseViewHolder> {

    public TestListAdapter() {
        super(R.layout.item_test_list);
    }

    @Override
    protected void convert(BaseViewHolder holder, JianDanMeiziData item) {
        holder.setText(R.id.tv_main, item.commentAuthor);
        holder.setText(R.id.tv_sub, item.commentAgent);

        if (ListUtils.isListHasData(item.pics)) {
            Glide.with(AppConfig.getContext())
                    .load(item.pics.get(0))
                    .asBitmap()
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into((ImageView) holder.getView(R.id.iv_avatar));
        }
    }
}
