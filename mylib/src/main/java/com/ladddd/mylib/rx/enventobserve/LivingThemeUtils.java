package com.ladddd.mylib.rx.enventobserve;

import android.content.Context;

import com.bilibili.magicasakura.utils.ThemeUtils;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by 陈伟达 on 2017/7/29.
 */

public class LivingThemeUtils {

    private static BehaviorSubject<Boolean> mSubject;

    public static BehaviorSubject<Boolean> getObservable() {
        if (mSubject == null) {
            mSubject = BehaviorSubject.createDefault(true);
        }
        return mSubject;
    }

    public static void refreshUiPositive(Context context, ThemeUtils.ExtraRefreshable refreshable) {
        ThemeUtils.refreshUI(context, refreshable);
        if (mSubject != null) {
            mSubject.onNext(false);
        }
    }

    public static void refreshUiNegative(Context context, ThemeUtils.ExtraRefreshable refreshable) {
        ThemeUtils.refreshUI(context, refreshable);
    }

}
