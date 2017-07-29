package com.ladddd.mylib.rx.enventobserve;

import android.content.Context;

import com.bilibili.magicasakura.utils.ThemeUtils;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by 陈伟达 on 2017/7/29.
 */

public class LivingThemeUtils {

    private static PublishSubject<Boolean> mSubject;

    public static Observable<Boolean> getObservable() {
        if (mSubject == null) {
            mSubject = PublishSubject.create();
        }
        return mSubject;
    }

    public static void refreshUiPositive(Context context, ThemeUtils.ExtraRefreshable refreshable) {
        ThemeUtils.refreshUI(context, refreshable);
        if (mSubject != null) {
            mSubject.onNext(true);
        }
    }

    public static void refreshUiNegative(Context context, ThemeUtils.ExtraRefreshable refreshable) {
        ThemeUtils.refreshUI(context, refreshable);
    }

}
