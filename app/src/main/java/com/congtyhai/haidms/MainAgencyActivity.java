package com.congtyhai.haidms;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.congtyhai.haidms.Agency.ShowAgencyActivity;
import com.congtyhai.haidms.Event.EventActivity;
import com.congtyhai.haidms.Util.NotificationActivity;
import com.congtyhai.haidms.calendar.StaffCalendarActivity;
import com.congtyhai.haidms.product.ProductTaskActivity;
import com.congtyhai.haidms.showinfo.CheckStaffActivity;
import com.congtyhai.haidms.showinfo.ShowBranchActivity;
import com.congtyhai.haidms.showinfo.ShowProductActivity;
import com.congtyhai.model.Realm.DTopicFirebase;
import com.congtyhai.model.api.MainAgencyInfoResult;
import com.congtyhai.model.api.MainInfoSend;
import com.congtyhai.util.HAIRes;
import com.congtyhai.util.RealmController;
import com.google.firebase.messaging.FirebaseMessaging;
import org.json.JSONArray;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainAgencyActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;

    TextView txtName;

    TextView txtCode;

    TextView txtType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_agency);
        ButterKnife.bind(this);
        createToolbar();
        createNavDraw();

        makeRequest();
    }


    private void makeRequest() {
        showpDialog();
        String tokenFirebase = prefsHelper.get(HAIRes.getInstance().PREF_KEY_FIREBASE, "");
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        final String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
        final int needUpdate = needUpdateDaily();
        MainInfoSend info = new MainInfoSend(user, token, tokenFirebase, needUpdate);
        Call<MainAgencyInfoResult> call = apiInterface().mainAgency(info);
        call.enqueue(new Callback<MainAgencyInfoResult>() {
            @Override
            public void onResponse(Call<MainAgencyInfoResult> call, Response<MainAgencyInfoResult> response) {

                if (response.body() != null) {
                    if ("1".equals(response.body().getId())) {
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

                        if (needUpdate == 1) {
                            saveListProduct(response.body().getProducts());
                            saveListProductGroup(response.body().getProductGroups());
                            updateDaily();
                        }

                        txtName.setText(response.body().getName());
                        txtCode.setText(response.body().getCode());
                        txtType.setText(response.body().getType());
                    }
                }
                initList();
                hidepDialog();
            }

            @Override
            public void onFailure(Call<MainAgencyInfoResult> call, Throwable t) {
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
                        MenuItem productManage = menu.findItem(R.id.nav_product_manage);
                        productManage.setVisible(true);
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
            commons.startActivity(MainAgencyActivity.this, ShowAgencyActivity.class);
        } else if (id == R.id.nav_staffcalendar) {
            commons.startActivity(MainAgencyActivity.this, StaffCalendarActivity.class);
        } else if (id == R.id.nav_product) {
            HAIRes.getInstance().inOder = 0;
            commons.startActivity(MainAgencyActivity.this, ShowProductActivity.class);
            //  showProductIntent.putExtra(HAIRes.getInstance().KEY_INTENT_ORDER, 0);
            // startActivity(showProductIntent);
        } else if (id == R.id.nav_product_manage) {
            commons.startActivity(MainAgencyActivity.this, ProductTaskActivity.class);
        } else if (id == R.id.nav_logout) {
            commons.showAlertCancel(MainAgencyActivity.this, "Cảnh báo", "Đăng xuất với tài khoản hiện tại", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    logout();
                }
            });
        } else if (id == R.id.nav_checkstaff) {
            commons.startActivity(MainAgencyActivity.this, CheckStaffActivity.class);
        } else if (id == R.id.nav_promotion) {
            commons.startActivity(MainAgencyActivity.this, EventActivity.class);
        } else if (id == R.id.nav_callcenter) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + HAIRes.getInstance().PHONE_CALL_CENTER));
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(intent);
            }

        }else if (id == R.id.nav_branch) {
            commons.startActivity(MainAgencyActivity.this, ShowBranchActivity.class);
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
                commons.startActivity(MainAgencyActivity.this, NotificationActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
