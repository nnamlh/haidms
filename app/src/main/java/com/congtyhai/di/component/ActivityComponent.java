package com.congtyhai.di.component;

import android.content.Context;

import com.congtyhai.di.module.ActivityModule;
import com.congtyhai.di.scope.ActivityContext;
import com.congtyhai.di.scope.ActivityScope;
import com.congtyhai.dms.BaseActivity;

import dagger.Component;

/**
 * Created by HAI on 8/10/2017.
 */

@ActivityScope
@Component(
        dependencies = ApplicationComponent.class,
        modules = {ActivityModule.class})
public interface ActivityComponent {

    @ActivityContext
    Context getContext();

    void inject(BaseActivity activity);

}