package com.ladddd.myandroidarch.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ladddd.myandroidarch.R;

/**
 * Created by 陈伟达 on 2017/6/22.
 */

public class TestAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {

    public TestAdapter() {
        super(R.layout.item_bar_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
        helper.setText(R.id.tv_content, item.toString());
    }
}
