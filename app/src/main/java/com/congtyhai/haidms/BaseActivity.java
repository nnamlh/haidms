package com.congtyhai.haidms;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.congtyhai.app.AppController;
import com.congtyhai.di.component.ActivityComponent;
import com.congtyhai.di.component.DaggerActivityComponent;
import com.congtyhai.di.module.ActivityModule;
import com.congtyhai.di.scope.RetrofitUploadInfo;
import com.congtyhai.haidms.login.LoginNameActivity;
import com.congtyhai.model.Realm.DTopicFirebase;
import com.congtyhai.model.api.AgencyC1Info;
import com.congtyhai.model.api.AgencyInfo;
import com.congtyhai.model.api.C2C1Info;
import com.congtyhai.model.api.GroupResultInfo;
import com.congtyhai.model.api.ProductCodeInfo;
import com.congtyhai.model.api.ReceiveInfo;
import com.congtyhai.model.api.ResultInfo;
import com.congtyhai.model.app.HaiLocation;
import com.congtyhai.service.GPSTracker;
import com.congtyhai.util.AnimationHelper;
import com.congtyhai.util.ApiInterface;
import com.congtyhai.util.Commons;
import com.congtyhai.util.HAIRes;
import com.congtyhai.util.LoginService;
import com.congtyhai.util.NotificationUtils;
import com.congtyhai.util.RealmController;
import com.congtyhai.util.ServiceGenerator;
import com.congtyhai.util.SharedPrefsHelper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by HAI on 8/7/2017.
 */

public class BaseActivity extends AppCompatActivity {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private android.support.v7.app.AlertDialog.Builder dNotification;

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

    protected Realm realmControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityComponent = DaggerActivityComponent.builder().applicationComponent(((AppController) getApplication()).getApplicationComponent()).activityModule(new ActivityModule(this)).build();

        activityComponent.inject(this);

        fireBaseBroadcast();

        pDialog = new ProgressDialog(BaseActivity.this);
        pDialog.setTitle("Đang xử lý...");
        pDialog.setCancelable(false);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //
        realmControl = RealmController.getInstance().getRealm();

    }

    private void fireBaseBroadcast() {
        // show dialog notification
        dNotification = new android.support.v7.app.AlertDialog.Builder(BaseActivity.this);
        dNotification.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(HAIRes.getInstance().REGISTRATION_COMPLETE)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(HAIRes.getInstance().TOPIC_GLOBAL);
                } else if (intent.getAction().equals(HAIRes.getInstance().PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                    String title = intent.getStringExtra("title");
                    showNotification(title, message);
                }
            }
        };

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
        gps = new GPSTracker(BaseActivity.this);
        if(gps.canGetLocation()) {
            return new HaiLocation(gps.getLatitude(), gps.getLongitude());
        } else {
            gps.showSettingsAlert();
            return new HaiLocation(0, 0);
        }
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
        try {
            Gson gson = new Gson();
            commons.writeFile(gson.toJson(agencies), HAIRes.getInstance().PATH_AGENCY_JSON, BaseActivity.this);
        } catch (Exception e) {

        }

    }

    protected void saveListC2OfC1(C2C1Info[] agencies) {
        try {
            Gson gson = new Gson();
            commons.writeFile(gson.toJson(agencies), HAIRes.getInstance().PATH_AGENCY_JSON, BaseActivity.this);
        } catch (Exception e) {

        }

    }

    protected List<C2C1Info> getListC2C1() {

        Gson gson = new Gson();
        try {

            BufferedReader reader = commons.readBufferedReader(HAIRes.getInstance().PATH_AGENCY_JSON, BaseActivity.this);

            if (reader != null) {
                Type listType = new TypeToken<List<C2C1Info>>() {
                }.getType();
                List<C2C1Info> agencyInfos = gson.fromJson(reader, listType);

                if(agencyInfos != null) {
                    return agencyInfos;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    protected void saveListProductGroup(GroupResultInfo[] groups) {

        try {
            Gson gson = new Gson();
            commons.writeFile(gson.toJson(groups), HAIRes.getInstance().PATH_PRODUCT_GROUP_JSON, BaseActivity.this);
        } catch (Exception e) {

        }
    }

    protected void saveListAgencyC1(AgencyC1Info[] agencies) {
        try {
            Gson gson = new Gson();
            commons.writeFile(gson.toJson(agencies), HAIRes.getInstance().PATH_AGENCY_C1_JSON, BaseActivity.this);
        } catch (Exception e) {

        }

    }

    protected List<AgencyInfo> getListAgency() {

        Gson gson = new Gson();
        try {

            BufferedReader reader = commons.readBufferedReader(HAIRes.getInstance().PATH_AGENCY_JSON, BaseActivity.this);

            if (reader != null) {
                Type listType = new TypeToken<List<AgencyInfo>>() {
                }.getType();
                List<AgencyInfo> agencyInfos = gson.fromJson(reader, listType);

                if(agencyInfos != null) {
                    return agencyInfos;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    protected List<GroupResultInfo> getListProductGroup() {

        Gson gson = new Gson();
        try {

            BufferedReader reader = commons.readBufferedReader(HAIRes.getInstance().PATH_PRODUCT_GROUP_JSON, BaseActivity.this);

            if (reader != null) {
                Type listType = new TypeToken<List<GroupResultInfo>>() {
                }.getType();
                List<GroupResultInfo> groups = gson.fromJson(reader, listType);

               if (groups != null)
                   return groups;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    protected List<ProductCodeInfo> getListProduct() {

        Gson gson = new Gson();
        try {

            BufferedReader reader = commons.readBufferedReader(HAIRes.getInstance().PATH_PRODUCT_JSON, BaseActivity.this);

            if (reader != null) {
                Type listType = new TypeToken<List<ProductCodeInfo>>() {
                }.getType();
                List<ProductCodeInfo> groups = gson.fromJson(reader, listType);

                if(groups != null)
                    return groups;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    protected List<AgencyC1Info> getListAgencyC1() {

        Gson gson = new Gson();
        try {

            BufferedReader reader = commons.readBufferedReader(HAIRes.getInstance().PATH_AGENCY_C1_JSON, BaseActivity.this);

            if (reader != null) {
                Type listType = new TypeToken<List<AgencyC1Info>>() {
                }.getType();
                List<AgencyC1Info> agencyInfos = gson.fromJson(reader, listType);

                if(agencyInfos != null)
                    return agencyInfos;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }



    protected void saveListProduct(final ProductCodeInfo[] productCodeInfos) {
        Gson gson = new Gson();
        commons.writeFile(gson.toJson(productCodeInfos), HAIRes.getInstance().PATH_PRODUCT_JSON, BaseActivity.this);
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


    protected int countDayInMonth(int year, int month) {
        String startDateString = String.format("%d/01/%d", month, year);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate;
        try {
            startDate = df.parse(startDateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);

            return calendar.get(Calendar.DAY_OF_MONTH);

        } catch (ParseException e) {
            return 1;
        }
    }

    protected int getCalendarMonth(int month) {
        switch (month) {
            case 1:
                return Calendar.JANUARY;
            case 2:
                return Calendar.FEBRUARY;
            case 3:
                return Calendar.MARCH;
            case 4:
                return Calendar.APRIL;
            case 5:
                return Calendar.MAY;
            case 6:
                return Calendar.JUNE;
            case 7:
                return Calendar.JULY;
            case 8:
                return Calendar.AUGUST;
            case 9:
                return Calendar.SEPTEMBER;
            case 10:
                return Calendar.OCTOBER;
            case 11:
                return Calendar.NOVEMBER;
            case 12:
                return Calendar.DECEMBER;
            default:
                return 1;
        }
    }


    //
    protected boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(HAIRes.getInstance().REGISTRATION_COMPLETE));

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(HAIRes.getInstance().PUSH_NOTIFICATION));

        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void showNotification(String title, String messenge) {

        dNotification.setTitle(title);
        dNotification.setMessage(messenge);
        dNotification.show();
    }

    protected void logout() {
        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
        LoginService apiUser = ServiceGenerator.createService(LoginService.class, user, token);


        Call<ResultInfo> call = apiUser.logout();

        call.enqueue(new Callback<ResultInfo>() {
            @Override
            public void onResponse(Call<ResultInfo> call,
                                   Response<ResultInfo> response) {
                hidepDialog();
                if (response.body() != null) {
                    //
                    List<DTopicFirebase> dTopicFirebases = RealmController.getInstance().getTopics();
                    for(DTopicFirebase topicFirebase : dTopicFirebases) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(topicFirebase.getName());
                    }

                    RealmController.getInstance().clearData(DTopicFirebase.class);
                    //
                    prefsHelper.deleteSavedData(HAIRes.getInstance().PREF_KEY_TOKEN);
                    prefsHelper.deleteSavedData(HAIRes.getInstance().PREF_KEY_USER);
                    prefsHelper.deleteSavedData(HAIRes.getInstance().PREF_KEY_TYPE);
                    prefsHelper.deleteSavedData(HAIRes.getInstance().PREF_KEY_UPDATE_DAILY);

                    // xoa file
                    commons.deleteFile(HAIRes.getInstance().PATH_AGENCY_C1_JSON, BaseActivity.this);
                    commons.deleteFile(HAIRes.getInstance().PATH_AGENCY_JSON, BaseActivity.this);
                    commons.deleteFile(HAIRes.getInstance().PATH_PRODUCT_GROUP_JSON, BaseActivity.this);
                    commons.deleteFile(HAIRes.getInstance().PATH_PRODUCT_JSON, BaseActivity.this);

                    Intent intent2 = new Intent(BaseActivity.this, LoginNameActivity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent2);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResultInfo> call, Throwable t) {
                hidepDialog();
            }
        });
    }


}
