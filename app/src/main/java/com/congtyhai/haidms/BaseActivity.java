package com.congtyhai.haidms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.Manifest;
import com.congtyhai.app.AppController;
import com.congtyhai.di.component.ActivityComponent;
import com.congtyhai.di.component.DaggerActivityComponent;
import com.congtyhai.di.module.ActivityModule;
import com.congtyhai.di.scope.RetrofitUploadInfo;
import com.congtyhai.util.AnimationHelper;
import com.congtyhai.util.ApiInterface;
import com.congtyhai.util.Commons;
import com.congtyhai.util.SharedPrefsHelper;
import com.sdsmdg.tastytoast.TastyToast;
import javax.inject.Inject;
import retrofit2.Retrofit;

/**
 * Created by HAI on 8/7/2017.
 */

public class BaseActivity extends AppCompatActivity implements LocationListener {

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

    private LocationManager locationManager;

    private String provider;

    protected double lat = 0;
    protected double lng = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityComponent = DaggerActivityComponent.builder().applicationComponent(((AppController) getApplication()).getApplicationComponent()).activityModule(new ActivityModule(this)).build();

        activityComponent.inject(this);

        pDialog = new ProgressDialog(BaseActivity.this);
        pDialog.setTitle("Đang xử lý...");
        pDialog.setCancelable(false);

        if (!checkGPS()) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();
                onLocationChanged(location);
            } else {
                commons.makeToast(getApplicationContext(), "Location not found", TastyToast.INFO).show();
            }
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

    private boolean checkGPS() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        return enabled;
    }


    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        } else {
            locationManager.requestLocationUpdates(provider, 400, 1, this);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }


    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        // Toast.makeText(this, "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        // Toast.makeText(this, "Disabled provider " + provider, Toast.LENGTH_SHORT).show();
    }

}
