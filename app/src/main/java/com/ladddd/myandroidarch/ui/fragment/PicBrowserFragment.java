package com.ladddd.myandroidarch.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.ladddd.myandroidarch.R;
import com.ladddd.mylib.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;

/**
 * Created by 陈伟达 on 2017/6/26.
 */

public class PicBrowserFragment extends BaseFragment {

    @BindView(R.id.iv_pic)
    PhotoView iv_pic;
    @BindView(R.id.rl_progress)
    RelativeLayout rl_progress;
    @BindView(R.id.tv_percent)
    TextView tv_percent;

    private String picUrl;

    public static PicBrowserFragment newInstance(String url) {
        PicBrowserFragment fragment = new PicBrowserFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_image_browser, new LinearLayout(getActivity()));
        ButterKnife.bind(this, contentView);

        picUrl = getArguments().getString("url");
//        picUrl = "http://www.noaanews.noaa.gov/stories/images/goes-12%2Dfirstimage-large081701%2Ejpg";

        ProgressManager.getInstance().addResponseListener(picUrl, new ProgressListener() {
            @Override
            public void onProgress(ProgressInfo progressInfo) {
                boolean show = progressInfo != null && progressInfo.getPercent() > 0 && progressInfo.getPercent() < 100;
                rl_progress.setVisibility(show?View.VISIBLE:View.GONE);
                if (show) {
                    tv_percent.setText(progressInfo.getPercent() + "%");
                }
            }

            @Override
            public void onError(long id, Exception e) {

            }
        });

        Glide.with(getActivity().getApplicationContext())
                .load(picUrl)
                .asBitmap()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(iv_pic);

        return contentView;
    }
}
