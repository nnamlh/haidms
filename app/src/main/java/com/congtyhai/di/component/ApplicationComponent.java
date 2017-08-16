package com.congtyhai.di.component;

import android.app.Application;
import android.content.Context;

import com.congtyhai.di.module.ApplicationModule;
import com.congtyhai.di.module.NetModule;
import com.congtyhai.di.module.UtilityModule;
import com.congtyhai.di.scope.ApplicationContext;
import com.congtyhai.di.scope.RetrofitUploadInfo;
import com.congtyhai.util.AnimationHelper;
import com.congtyhai.util.Commons;
import com.congtyhai.util.SharedPrefsHelper;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by HAI on 8/10/2017.
 */

@Singleton
@Component(modules = {
        ApplicationModule.class,
        UtilityModule.class, NetModule.class
})
public interface ApplicationComponent {

    @ApplicationContext
    Context getContext();

    void inject(Application application);

    // Utility Module
    Commons getCommonUtils();


    Retrofit getRetrofit();

    @RetrofitUploadInfo
    Retrofit getRetrofitUpload();


    SharedPrefsHelper getSharedPrefsHelper();

    AnimationHelper getAnimHelper();
}