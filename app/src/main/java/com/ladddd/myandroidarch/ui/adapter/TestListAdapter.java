package com.ladddd.myandroidarch.ui.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ladddd.myandroidarch.R;
import com.ladddd.myandroidarch.model.JianDanMeizi.JianDanMeiziData;
import com.ladddd.mylib.config.AppConfig;
import com.ladddd.mylib.utils.ListUtils;

import java.util.List;

/**
 * Created by 陈伟达 on 2017/5/22.
 */

public class TestListAdapter extends BaseMultiItemQuickAdapter<JianDanMeiziData, BaseViewHolder> {

    public TestListAdapter(List<JianDanMeiziData> data) {
        super(data);
        addItemType(JianDanMeiziData.NORMAL, R.layout.item_test_list);
        addItemType(JianDanMeiziData.HORIZON_LIST, R.layout.item_horizon_list);
    }

    @Override
    protected void convert(BaseViewHolder holder, JianDanMeiziData item) {
        switch (holder.getItemViewType()) {
            case JianDanMeiziData.NORMAL:
                handleNormal(holder, item);
                break;
            case JianDanMeiziData.HORIZON_LIST:
                handleHorizonList(holder, item);
                break;
        }
    }

    private void handleNormal(BaseViewHolder holder, JianDanMeiziData item) {
        holder.setText(R.id.tv_main, item.commentAuthor);
        holder.setText(R.id.tv_sub, item.commentAgent);

        if (ListUtils.isListHasData(item.pics)) {
            Glide.with(mContext.getApplicationContext())
                    .load(item.pics.get(0))
                    .asBitmap()
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into((ImageView) holder.getView(R.id.iv_avatar));
        }
    }

    private void handleHorizonList(BaseViewHolder holder, JianDanMeiziData item) {
        RecyclerView recyclerView = holder.getView(R.id.recyclerView);
        if (null == recyclerView.getLayoutManager()) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        }
        HorizonListAdapter adapter = (HorizonListAdapter) recyclerView.getAdapter();
        if (null == adapter) {
            adapter = new HorizonListAdapter();
            recyclerView.setAdapter(adapter);
        }
        for (int i = 0; i < 10; i++) {
            item.pics.add(item.pics.get(0));
        }
        adapter.setNewData(item.pics);
    }
}
