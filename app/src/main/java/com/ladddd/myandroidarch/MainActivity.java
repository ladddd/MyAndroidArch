package com.ladddd.myandroidarch;

import android.content.Intent;
import android.widget.Button;

import com.ladddd.mylib.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_ptr) Button btnGotoPtr;
    @BindView(R.id.btn_share) Button btnShowShare;

    @OnClick(R.id.btn_ptr) void goToPtr() {
        PtrActivity.open(this);
    }
    @OnClick(R.id.btn_share) void showSharePanel() {
//        Intent sendIntent = new Intent();
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//        sendIntent.setType("text/plain");
//        startActivity(sendIntent);

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onBackPressed() {
        //hide mainActivity to background, not fully quit
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.addCategory(Intent.CATEGORY_HOME);
        startActivity(launcherIntent);
    }
}
