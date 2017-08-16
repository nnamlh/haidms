package com.congtyhai.di.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.animation.Animation;

import com.congtyhai.util.AnimationHelper;
import com.congtyhai.util.Commons;
import com.congtyhai.util.SharedPrefsHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by HAI on 8/10/2017.
 */
@Module
public class UtilityModule {

    @Provides
    @Singleton
    public Commons provideCommonUtils() {
        return new Commons();
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPrefs(Application mApplication) {
        return mApplication.getSharedPreferences("hai-prefs", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    public AnimationHelper animHelper() {
        return new AnimationHelper();
    }
}



