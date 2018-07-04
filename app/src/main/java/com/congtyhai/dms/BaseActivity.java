package com.congtyhai.dms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.congtyhai.app.AppController;
import com.congtyhai.di.component.ActivityComponent;
import com.congtyhai.di.component.DaggerActivityComponent;
import com.congtyhai.di.module.ActivityModule;
import com.congtyhai.di.scope.RetrofitUploadInfo;
import com.congtyhai.dms.login.LoginNameActivity;
import com.congtyhai.model.Realm.DTopicFirebase;
import com.congtyhai.model.api.AgencyC1Info;
import com.congtyhai.model.api.AgencyInfo;
import com.congtyhai.model.api.C2C1Info;
import com.congtyhai.model.api.GroupResultInfo;
import com.congtyhai.model.api.ProductCodeInfo;
import com.congtyhai.model.api.ResultInfo;
import com.congtyhai.util.AnimationHelper;
import com.congtyhai.util.ApiInterface;
import com.congtyhai.util.Commons;
import com.congtyhai.util.HAIRes;
import com.congtyhai.util.LoginService;
import com.congtyhai.util.NotificationUtils;
import com.congtyhai.util.RealmController;
import com.congtyhai.util.ServiceGenerator;
import com.congtyhai.util.SharedPrefsHelper;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

    private static final String TAG = BaseActivity.class.getSimpleName();

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private android.support.v7.app.AlertDialog.Builder dNotification;

    @Inject
    public Retrofit retrofit;

    @Inject
    @RetrofitUploadInfo
    protected Retrofit retrofitUpload;


    @Inject
    public Commons commons;

    @Inject
    public SharedPrefsHelper prefsHelper;

    @Inject
    protected AnimationHelper animHelper;

    protected ActivityComponent activityComponent;

    private ProgressDialog pDialog;

    protected Toolbar toolbar;

    protected Realm realmControl;

    // location
    /**
     * Constant used in the location settings dialog.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient mSettingsClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;

    /**
     * Represents a geographical location.
     */
   // private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityComponent = DaggerActivityComponent.builder().applicationComponent(((AppController) getApplication()).getApplicationComponent()).activityModule(new ActivityModule(this)).build();

        activityComponent.inject(this);

        fireBaseBroadcast();

        pDialog = new ProgressDialog(BaseActivity.this);
        pDialog.setTitle("Đang xử lý...");
        pDialog.setCancelable(false);
        //
        realmControl = RealmController.getInstance().getRealm();

        // location
        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        mRequestingLocationUpdates = false;
        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();

        startLocationUpdates();

    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                HAIRes.getInstance().mCurrentLocation = locationResult.getLastLocation();
               // Log.d("Location", mCurrentLocation.getLatitude() + " - " + mCurrentLocation.getLongitude());
                    }
        };
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }


    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {

            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }
            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                HAIRes.getInstance().mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }
        }
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

    public double getLat(){
        if (HAIRes.getInstance().mCurrentLocation != null)
            return  HAIRes.getInstance().mCurrentLocation.getLatitude();

        return 0;
    }

    public double getLng() {
        if(HAIRes.getInstance().mCurrentLocation != null)
            return HAIRes.getInstance().mCurrentLocation.getLongitude();
        return 0;
    }

    // dialog
    public void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    public void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    // api
    public ApiInterface apiInterface() {
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

        // Within {@code onPause()}, we remove location updates. Here, we resume receiving
        // location updates if the user has requested them.
        if (!mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        stopLocationUpdates();
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


    protected String getFirebaseReg() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(HAIRes.getInstance().SHARED_PREF, 0);
        String token = pref.getString("regId", "");

        return  token;
    }

    /**
     * Stores activity data in the Bundle.
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, HAIRes.getInstance().mCurrentLocation);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        commons.showAlertCancel(BaseActivity.this, "Cảnh báo", "Ứng dụng cần mở GPS khi sử dụng", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    Status status = new Status(LocationSettingsStatusCodes.RESOLUTION_REQUIRED);
                                    status.startResolutionForResult(BaseActivity.this, REQUEST_CHECK_SETTINGS);

                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                            }
                        });
                        break;
                }
                break;
        }
    }

    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    private void startLocationUpdates() {
        mRequestingLocationUpdates = true;
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");
                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(BaseActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                Toast.makeText(BaseActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                        }
                    }
                });
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates() {

        if (!mRequestingLocationUpdates) {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }

        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdates = false;
                    }
                });
    }


}
