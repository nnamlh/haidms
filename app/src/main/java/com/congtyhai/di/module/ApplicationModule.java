package com.congtyhai.di.module;

import android.app.Application;
import android.content.Context;

import com.congtyhai.di.scope.ApplicationContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by HAI on 8/10/2017.
 */

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(final Application application) {
        mApplication = application;
    }

    @Provides
    public Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    public Context provideApplicationContext() {
        return mApplication;
    }


}