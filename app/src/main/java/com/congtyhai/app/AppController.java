package com.congtyhai.app;

import android.app.Application;

import com.congtyhai.di.component.ApplicationComponent;
import com.congtyhai.di.component.DaggerApplicationComponent;
import com.congtyhai.di.module.ApplicationModule;
import com.congtyhai.di.module.NetModule;
import com.congtyhai.di.module.UtilityModule;

import io.realm.Realm;

/**
 * Created by HAI on 8/7/2017.
 */

public class AppController extends Application {

    ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this))
                .utilityModule(new UtilityModule()).netModule(new NetModule()).build();
    }


    public ApplicationComponent getApplicationComponent() {
        return  applicationComponent;
    }
}
