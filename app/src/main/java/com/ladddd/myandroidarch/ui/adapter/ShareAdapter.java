package com.ladddd.myandroidarch.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ladddd.myandroidarch.R;
import com.ladddd.myandroidarch.model.ShareAppInfo;

/**
 * Created by 陈伟达 on 2017/6/5.
 */

public class ShareAdapter extends BaseQuickAdapter<ShareAppInfo, BaseViewHolder> {

    public ShareAdapter() {
        super(R.layout.item_share_app);
    }

    @Override
    protected void convert(BaseViewHolder holder, ShareAppInfo item) {
        holder.setText(R.id.tv_name, item.getLabel());
        holder.setImageDrawable(R.id.iv_logo, item.getIcon());
    }
}
