package com.congtyhai.di.module;

import android.app.Activity;
import android.content.Context;

import com.congtyhai.di.scope.ActivityContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by HAI on 8/10/2017.
 */

@Module
public class ActivityModule {
    private final Activity mActivity;

    public ActivityModule(final Activity activity) {
        mActivity = activity;
    }

    @Provides
    public Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityContext
    public Context provideActivityContext() {
        return mActivity;
    }
}