package com.ladddd.myandroidarch.ui.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.ladddd.myandroidarch.R;
import com.ladddd.mylib.widget.SelectableTabView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈伟达 on 2017/6/23.
 *
 */

public class HomeBottomSheet extends CardView {

    private List<SelectableTabView> tabArray = new ArrayList<>();

    public HomeBottomSheet(Context context) {
        super(context);
        init(context);
    }

    public HomeBottomSheet(Context context, AttributeSet attrs) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet, this);
        SelectableTabView tab1 = (SelectableTabView) contentView.findViewById(R.id.tab1);
        SelectableTabView tab2 = (SelectableTabView) contentView.findViewById(R.id.tab2);
        SelectableTabView tab3 = (SelectableTabView) contentView.findViewById(R.id.tab3);
        SelectableTabView tab4 = (SelectableTabView) contentView.findViewById(R.id.tab4);

        tabArray.add(tab1);
        tabArray.add(tab2);
        tabArray.add(tab3);
        tabArray.add(tab4);

        for (int i = 0; i < tabArray.size(); i++) {
            final int finalI = i;
            tabArray.get(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTabSelected(finalI);
                }
            });
        }
    }

    private void setTabSelected(int index) {
        for (int i = 0; i < tabArray.size(); i++) {
            if (i != index) {
                tabArray.get(i).setTabUnSelected();
            } else {
                tabArray.get(i).setTabSelected();
            }
        }
    }
}
