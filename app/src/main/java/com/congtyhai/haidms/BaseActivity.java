package com.congtyhai.haidms;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.congtyhai.app.AppController;
import com.congtyhai.di.component.ActivityComponent;
import com.congtyhai.di.component.DaggerActivityComponent;
import com.congtyhai.di.module.ActivityModule;
import com.congtyhai.di.scope.RetrofitUploadInfo;
import com.congtyhai.util.AnimationHelper;
import com.congtyhai.util.ApiInterface;
import com.congtyhai.util.Commons;
import com.congtyhai.util.SharedPrefsHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by HAI on 8/7/2017.
 */

public class BaseActivity extends AppCompatActivity {

    @Inject
    protected Retrofit retrofit;

    @Inject
    @RetrofitUploadInfo
    protected Retrofit retrofitUpload;


    @Inject
    protected Commons commons;

    @Inject
    protected SharedPrefsHelper prefsHelper;

    @Inject
    protected AnimationHelper animHelper;

    protected ActivityComponent activityComponent;

    private ProgressDialog pDialog;

    protected Toolbar toolbar;

    public BaseActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityComponent = DaggerActivityComponent.builder().applicationComponent(((AppController) getApplication()).getApplicationComponent()).activityModule(new ActivityModule(this)).build();

        activityComponent.inject(this);

        pDialog = new ProgressDialog(BaseActivity.this);
        pDialog.setTitle("Đang xử lý...");
        pDialog.setCancelable(false);
    }

    // dialog
    protected void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    protected void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    // api
    protected ApiInterface apiInterface() {
        return retrofit.create(ApiInterface.class);
    }


    protected ApiInterface apiInterfaceUpload() {
        return retrofitUpload.create(ApiInterface.class);
    }

    //
    protected void createToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
}
