package com.ladddd.myandroidarch.activity;

import android.content.Intent;

import com.ladddd.myandroidarch.BuildConfig;
import com.ladddd.myandroidarch.R;
import com.ladddd.myandroidarch.ui.activity.MainActivity;
import com.ladddd.myandroidarch.ui.activity.PtrActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Java6Assertions.assertThat;  //  some jre7/jre8 method not exist in art or dalvik vm
//                                                                  detail see https://github.com/joel-costigliola/assertj-core/issues/345
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by 陈伟达 on 2017/7/13.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityTest {

    @Before
    public void setUp() {

    }

    @Test
    public void clickingPtr_shouldStartPtrActivity() {
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.findViewById(R.id.btn_ptr).performClick();

        Intent expectedIntent = new Intent(mainActivity, PtrActivity.class);
        assertThat(shadowOf(mainActivity).getNextStartedActivity().filterEquals(expectedIntent)); //how to compare intent equal
    }
}
