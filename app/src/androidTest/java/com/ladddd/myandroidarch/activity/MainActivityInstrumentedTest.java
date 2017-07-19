package com.ladddd.myandroidarch.activity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ladddd.myandroidarch.ui.activity.StorageTestActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by 陈伟达 on 2017/7/14.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<StorageTestActivity> mActivityTestRule = new ActivityTestRule<>(StorageTestActivity.class);

}
