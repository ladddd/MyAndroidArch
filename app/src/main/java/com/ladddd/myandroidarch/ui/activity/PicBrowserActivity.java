package com.ladddd.myandroidarch.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;

import com.ladddd.myandroidarch.R;
import com.ladddd.myandroidarch.ui.fragment.PicBrowserFragment;
import com.ladddd.mylib.BaseActivity;
import com.ladddd.mylib.widget.viewpager.DepthTransformer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 陈伟达 on 2017/6/26.
 */

public class PicBrowserActivity extends BaseActivity {

    private ArrayList<String> imageList;
    private int startIndex;

    @BindView(R.id.vp_pics)
    ViewPager vp_pics;

    public static void launch(Context context, ArrayList<String> imageList, int startIndex) {
        Intent intent = new Intent(context, PicBrowserActivity.class);
        intent.putExtra("imageList", imageList);
        intent.putExtra("startIndex", startIndex);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_pic_browser);
        ButterKnife.bind(this);

        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
    }

    @Override
    protected void initData() {
        imageList = getIntent().getStringArrayListExtra("imageList");
        startIndex = getIntent().getIntExtra("startIndex", 0);

//        vp_pics.setOffscreenPageLimit(ListUtils.isListHasData(imageList)?imageList.size():1);
        vp_pics.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return PicBrowserFragment.newInstance(imageList.get(position));
            }

            @Override
            public int getCount() {
                if (null == imageList) {
                    return 0;
                }
                return imageList.size();
            }
        });
        vp_pics.setPageTransformer(true, new DepthTransformer());
        vp_pics.setCurrentItem(startIndex);
    }
}
