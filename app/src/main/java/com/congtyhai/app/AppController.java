package com.congtyhai.app;

import android.app.Application;

import com.congtyhai.di.component.ApplicationComponent;
import com.congtyhai.di.component.DaggerApplicationComponent;
import com.congtyhai.di.module.ApplicationModule;
import com.congtyhai.di.module.NetModule;
import com.congtyhai.di.module.UtilityModule;
import com.squareup.leakcanary.LeakCanary;

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
        initLeakCanary();
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this))
                .utilityModule(new UtilityModule()).netModule(new NetModule()).build();
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }

    public ApplicationComponent getApplicationComponent() {
        return  applicationComponent;
    }
}
