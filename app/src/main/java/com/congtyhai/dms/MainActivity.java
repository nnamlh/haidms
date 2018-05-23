package com.congtyhai.dms;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.MenuItem;
import android.widget.TextView;

import com.congtyhai.dms.Agency.ShowAgencyActivity;
import com.congtyhai.dms.Event.EventActivity;
import com.congtyhai.dms.Util.NotificationActivity;
import com.congtyhai.dms.calendar.StaffCalendarActivity;
import com.congtyhai.dms.checkin.CheckInActivity;
import com.congtyhai.dms.manageorders.StaffOrderActivity;
import com.congtyhai.dms.product.ProductTaskActivity;
import com.congtyhai.dms.report.KPIActivity;
import com.congtyhai.dms.showinfo.CheckStaffActivity;
import com.congtyhai.dms.showinfo.ShowBranchActivity;
import com.congtyhai.dms.showinfo.ShowProductActivity;
import com.congtyhai.model.Realm.DTopicFirebase;
import com.congtyhai.model.api.AgencyInfo;
import com.congtyhai.model.api.MainInfoResult;
import com.congtyhai.model.api.MainInfoSend;
import com.congtyhai.model.app.CheckInFunctionInfo;
import com.congtyhai.util.HAIRes;
import com.congtyhai.util.RealmController;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private GoogleMap mMap;

    NavigationView navigationView;

    TextView txtName;

    TextView txtCode;

    TextView txtType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        createToolbar();

        createNavDraw();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        makeRequest();
    }

    public void showCheckIn(View view) {
        commons.startActivity(MainActivity.this, CheckInActivity.class);
    }

    private void makeRequest() {
        showpDialog();
        String tokenFirebase = getFirebaseReg();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        final String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
        final int needUpdate = needUpdateDaily();
        MainInfoSend info = new MainInfoSend(user, token, tokenFirebase, needUpdate);
        Call<MainInfoResult> call = apiInterface().updateReg(info);
        call.enqueue(new Callback<MainInfoResult>() {
            @Override
            public void onResponse(Call<MainInfoResult> call, Response<MainInfoResult> response) {

                if (response.body() != null) {
                    if ("1".equals(response.body().getId())) {
                        txtName.setText(response.body().getName());
                        txtCode.setText(response.body().getCode());
                        txtType.setText(response.body().getType());

                        RealmController.getInstance().clearData(DTopicFirebase.class);
                        for (final String topic : response.body().getTopics()) {
                            FirebaseMessaging.getInstance().subscribeToTopic(topic);
                            realmControl.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    DTopicFirebase data = realm.createObject(DTopicFirebase.class);
                                    data.setName(topic);

                                }
                            });
                        }
                        setListMainFunction(response.body().getFunction());
                        /*
                        if (needUpdate == 1) {
                            saveListAgency(response.body().getAgencies());
                            saveListProduct(response.body().getProducts());
                            saveListProductGroup(response.body().getProductGroups());
                            updateDaily();
                        }
                        saveListAgencyC1(response.body().getAgencyc1());
                        */
                    }

                    initList();
                    hidepDialog();
                    new ReadDataTask().execute();
                } else {
                    commons.showAlertInfo(MainActivity.this, "Thông báo", "Ứng dụng đang nâng cấp", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<MainInfoResult> call, Throwable t) {
                //initList();
                hidepDialog();
                commons.showAlertInfo(MainActivity.this, "Thông báo", "Mất kết nối 3G hoặc Internet trên điện thoại", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
            }
        });
    }

    private void initList() {
        String function = getListMainFunction();
        Menu menu = navigationView.getMenu();
        try {

            JSONArray jsonArray = new JSONArray(function);
            for (int i = 0; i < jsonArray.length(); i++) {
                String item = jsonArray.getString(i);
                switch (item) {
                    case "checkstaff":
                        MenuItem checkStaffItem = menu.findItem(R.id.nav_checkstaff);
                        checkStaffItem.setVisible(true);
                        break;
                    case "event":
                        MenuItem event = menu.findItem(R.id.nav_promotion);
                        event.setVisible(true);
                        break;
                    case "listproduct":
                        MenuItem productlist = menu.findItem(R.id.nav_product);
                        productlist.setVisible(true);
                        break;
                    case "listagency":
                        MenuItem agency = menu.findItem(R.id.nav_customer);
                        agency.setVisible(true);
                        break;
                }
            }


        } catch (Exception e) {

        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(3.0f);
        mMap.setMaxZoomPreference(20.0f);
        LatLng me = new LatLng(getLat(), getLng());
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {

                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.infowindow_item, null);

                TextView name = (TextView) v.findViewById(R.id.txtname);
                name.setText(marker.getTitle());

                TextView notes = (TextView) v.findViewById(R.id.txtcode);
                notes.setText(marker.getSnippet());

                TextView distance = (TextView) v.findViewById(R.id.txtdistance);

                double d = commons.distance(getLat(), getLng(), marker.getPosition().latitude, marker.getPosition().longitude);


                distance.setText(HAIRes.getInstance().getConvertMesterDistance(d));
                return v;
            }
        });
        mMap.addMarker(new MarkerOptions().position(me).title("VỊ TRÍ CỦA TÔI").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(me));

    }

    private class ReadDataTask extends AsyncTask<String, Integer, List<AgencyInfo>> {
        protected List<AgencyInfo> doInBackground(String... urls) {

            List<AgencyInfo> data = new ArrayList<>();
            try {

                data = getListAgency();

            } catch (Exception e) {

            }
            return data;
        }

        @Override
        protected void onPreExecute() {
            showpDialog();
        }

        protected void onPostExecute(List<AgencyInfo> result) {

           if (result != null && mMap != null){

               for (AgencyInfo info : result) {
                   LatLng me = new LatLng(info.getLat(), info.getLng());
                   mMap.addMarker(new MarkerOptions().position(me).title(info.getDeputy() + " - " + info.getCode()).snippet(info.getName()));
               }

           }
            hidepDialog();
        }
    }

    private void createNavDraw() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        View viewHeader = (View) navigationView.getHeaderView(0);
        txtName = (TextView) viewHeader.findViewById(R.id.name);
        txtCode = (TextView) viewHeader.findViewById(R.id.code);
        txtType = (TextView) viewHeader.findViewById(R.id.type);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_customer) {
            commons.startActivity(MainActivity.this, ShowAgencyActivity.class);
        } else if (id == R.id.nav_staffcalendar) {
            commons.startActivity(MainActivity.this, StaffCalendarActivity.class);
        }  else if (id == R.id.nav_product_manage) {
            commons.startActivity(MainActivity.this, ProductTaskActivity.class);
        } else if (id == R.id.nav_logout) {
            commons.showAlertCancel(MainActivity.this, "Cảnh báo", "Đăng xuất với tài khoản hiện tại", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    logout();
                }
            });
        } else if (id == R.id.nav_checkstaff) {
            commons.startActivity(MainActivity.this, CheckStaffActivity.class);
        } else if (id == R.id.nav_promotion) {
            commons.startActivity(MainActivity.this, EventActivity.class);
        } else if (id == R.id.nav_callcenter) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + HAIRes.getInstance().PHONE_CALL_CENTER));
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(intent);
            }
        } else if (id == R.id.nav_branch) {
            commons.startActivity(MainActivity.this, ShowBranchActivity.class);
        } else if (id == R.id.nav_order) {
            commons.startActivity(MainActivity.this, StaffOrderActivity.class);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.notification_action:
                commons.startActivity(MainActivity.this, NotificationActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
