package com.congtyhai.haidms;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.congtyhai.app.AppController;
import com.congtyhai.di.component.ActivityComponent;
import com.congtyhai.di.component.DaggerActivityComponent;
import com.congtyhai.di.module.ActivityModule;
import com.congtyhai.di.scope.RetrofitUploadInfo;
import com.congtyhai.model.api.AgencyInfo;
import com.congtyhai.model.api.ProductCodeInfo;
import com.congtyhai.model.api.ReceiveInfo;
import com.congtyhai.model.app.HaiLocation;
import com.congtyhai.service.GPSTracker;
import com.congtyhai.util.AnimationHelper;
import com.congtyhai.util.ApiInterface;
import com.congtyhai.util.Commons;
import com.congtyhai.util.HAIRes;
import com.congtyhai.util.SharedPrefsHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

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

    // GPSTracker class
    protected GPSTracker gps;

    protected LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityComponent = DaggerActivityComponent.builder().applicationComponent(((AppController) getApplication()).getApplicationComponent()).activityModule(new ActivityModule(this)).build();

        activityComponent.inject(this);

        pDialog = new ProgressDialog(BaseActivity.this);
        pDialog.setTitle("Đang xử lý...");
        pDialog.setCancelable(false);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //

    }

    protected void createLocation() {
        gps = new GPSTracker(BaseActivity.this);
        checkLocation();
    }

    protected boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    protected boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    protected HaiLocation getCurrentLocation() {
        return new HaiLocation(gps.getLatitude(), gps.getLongitude());
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

    protected void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(BaseActivity.this);
        dialog.setTitle("Enable Location")
                .setMessage("Cho phép lấy thông tin GPS từ điện thoại.")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
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


    protected int needUpdateDaily() {
        String timeStamp = new SimpleDateFormat("ddMMyyyy",
                Locale.getDefault()).format(new Date());
        String data = prefsHelper.get(HAIRes.getInstance().PREF_KEY_UPDATE_DAILY, "");
        if (timeStamp.equals(data))
            return 0;
        else
            return 1;
    }

    protected void saveListAgency(AgencyInfo[] agencies) {
        Gson gson = new Gson();
        commons.writeFile(gson.toJson(agencies), HAIRes.getInstance().PATH_AGENCY_JSON);
    }

    protected List<ReceiveInfo> getListReceive() {
        Gson gson = new Gson();
        try {

            File file = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    "HAI");
            BufferedReader br = new BufferedReader(
                    new FileReader(file.getAbsoluteFile() + HAIRes.getInstance().PATH_RECEIVE_JSON));

            Type listType = new TypeToken<List<ReceiveInfo>>() {
            }.getType();
            return gson.fromJson(br, listType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    protected List<AgencyInfo> getListAgency() {

        Gson gson = new Gson();
        try {

            BufferedReader reader = commons.readBufferedReader(HAIRes.getInstance().PATH_AGENCY_JSON);

            if (reader != null) {
                Type listType = new TypeToken<List<AgencyInfo>>() {
                }.getType();
                List<AgencyInfo> agencyInfos = gson.fromJson(reader, listType);

                return agencyInfos;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    protected void saveListReceive(ReceiveInfo[] receiveInfo) {
        Gson gson = new Gson();
        commons.writeFile(gson.toJson(receiveInfo), HAIRes.getInstance().PATH_RECEIVE_JSON);
    }

    protected void saveListProduct(final ProductCodeInfo[] productCodeInfos) {
        Gson gson = new Gson();
        commons.writeFile(gson.toJson(productCodeInfos), HAIRes.getInstance().PATH_PRODUCT_JSON);
    }

    protected void updateDaily() {
        String timeStamp = new SimpleDateFormat("ddMMyyyy",
                Locale.getDefault()).format(new Date());
        prefsHelper.put(HAIRes.getInstance().PREF_KEY_UPDATE_DAILY, timeStamp);
    }

    protected void setListMainFunction(String funcs) {
        prefsHelper.put(HAIRes.getInstance().PREF_KEY_FUNCTION, funcs);
    }

    protected String getListMainFunction() {
        return prefsHelper.get(HAIRes.getInstance().PREF_KEY_FUNCTION, "");
    }

}
