package com.congtyhai.haidms;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.congtyhai.adapter.CheckinFunctionAdapter;
import com.congtyhai.haidms.Agency.ShowAgencyActivity;
import com.congtyhai.haidms.calendar.StaffCalendarActivity;
import com.congtyhai.haidms.checkin.CheckInActivity;
import com.congtyhai.haidms.showinfo.ShowProductActivity;
import com.congtyhai.model.api.AgencyInfo;
import com.congtyhai.model.api.MainInfoResult;
import com.congtyhai.model.api.MainInfoSend;
import com.congtyhai.model.app.CheckInFunctionInfo;
import com.congtyhai.model.app.HaiLocation;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.NonScrollListView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, AdapterView.OnItemClickListener {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    private GoogleMap mMap;
    BottomSheetDialog mBottomSheetDialog;
    List<CheckInFunctionInfo> checkInFunctionInfos;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        createToolbar();
        fabClick();
        createNavDraw();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        createLocation();
        //  createBottomSheet();
        makeRequest();
    }


    private void makeRequest() {
        showpDialog();
        String tokenFirebase = prefsHelper.get(HAIRes.getInstance().PREF_KEY_FIREBASE, "");
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
        final int needUpdate = needUpdateDaily();
        MainInfoSend info = new MainInfoSend(user, token, tokenFirebase, needUpdate);
        Call<MainInfoResult> call = apiInterface().updateReg(info);
        call.enqueue(new Callback<MainInfoResult>() {
            @Override
            public void onResponse(Call<MainInfoResult> call, Response<MainInfoResult> response) {

                if (response.body() != null) {
                    if ("1".equals(response.body().getId())) {
                        for (String topic : response.body().getTopics()) {
                            //  FirebaseMessaging.getInstance().subscribeToTopic(topic);
                        }
                        //  storeTopicInPref(response.body().getTopics());
                        setListMainFunction(response.body().getFunction());
                        if (needUpdate == 1) {
                            saveListAgency(response.body().getAgencies());
                          //  saveListReceive(response.body().getRecivers());
                            saveListProduct(response.body().getProducts());
                            saveListProductGroup(response.body().getProductGroups());
                            saveListAgencyC1(response.body().getAgencyc1());
                            updateDaily();
                        }
                    }
                }
                initList();
                hidepDialog();
                new ReadDataTask().execute();
            }

            @Override
            public void onFailure(Call<MainInfoResult> call, Throwable t) {
                initList();
                hidepDialog();
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
                    case "product":
                        MenuItem productManage = menu.findItem(R.id.nav_product);
                        productManage.setVisible(true);
                        break;
                    case "event":
                        MenuItem event = menu.findItem(R.id.nav_promotion);
                        event.setVisible(true);
                        break;
                    case "newfeed":
                        MenuItem newFeed = menu.findItem(R.id.nav_notice);
                        newFeed.setVisible(true);
                        break;
                }
            }


        } catch (Exception e) {

        }

    }


    private void createBottomSheet() {
        mBottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.MaterialDialogSheet);
        View sheetView = MainActivity.this.getLayoutInflater().inflate(R.layout.checkin_bottom_menu, null);
        ImageView imgClose = (ImageView) sheetView.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
                fab.setVisibility(View.VISIBLE);
            }
        });

        NonScrollListView listView = (NonScrollListView) sheetView.findViewById(R.id.list);
        checkInFunctionInfos = new ArrayList<CheckInFunctionInfo>();
        checkInFunctionInfos.add(new CheckInFunctionInfo(HAIRes.getInstance().CHECKIN_CHECK, R.drawable.ic_menu_send, "Ghé thăm", ""));
        checkInFunctionInfos.add(new CheckInFunctionInfo(HAIRes.getInstance().CHECKIN_PRODUCT, R.drawable.ic_menu_send, "Sản phẩm", ""));
        CheckinFunctionAdapter adapter = new CheckinFunctionAdapter(MainActivity.this, checkInFunctionInfos);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.setCancelable(false);
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
        mMap.setMinZoomPreference(5.0f);
        mMap.setMaxZoomPreference(16.0f);
        HaiLocation location = getCurrentLocation();
        LatLng me = new LatLng(location.getLatitude(), location.getLongitude());
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

                long d = commons.distance(getCurrentLocation().getLatitude(), getCurrentLocation().getLongitude(), marker.getPosition().latitude, marker.getPosition().longitude, "M");
                distance.setText(d + " m");
                return v;
            }
        });
        //mMap.addMarker(new MarkerOptions().position(me).title("ME").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
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
            for (AgencyInfo info : result) {
                LatLng me = new LatLng(info.getLat(), info.getLng());
                mMap.addMarker(new MarkerOptions().position(me).title(info.getDeputy() + " - " + info.getCode()).snippet(info.getName()));
            }


            hidepDialog();
        }
    }

    private void fabClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // mBottomSheetDialog.show();
                // fab.setVisibility(View.GONE);
                commons.startActivity(MainActivity.this, CheckInActivity.class);
            }
        });
    }

    private void createNavDraw() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_customer) {
            commons.startActivity(MainActivity.this, ShowAgencyActivity.class);
        } else if (id == R.id.nav_staffcalendar) {
            commons.startActivity(MainActivity.this, StaffCalendarActivity.class);
        } else if (id == R.id.nav_product){
            commons.startActivity(MainActivity.this, ShowProductActivity.class);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CheckInFunctionInfo info = checkInFunctionInfos.get(i);
        if (info.getCode() == HAIRes.getInstance().CHECKIN_CHECK) {
            commons.startActivity(MainActivity.this, CheckInActivity.class);
        }
        mBottomSheetDialog.dismiss();
        fab.setVisibility(View.VISIBLE);
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
