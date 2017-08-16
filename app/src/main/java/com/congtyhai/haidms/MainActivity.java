package com.congtyhai.haidms;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.View;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.congtyhai.adapter.CheckinFunctionAdapter;
import com.congtyhai.model.app.CheckInFunctionInfo;
import com.congtyhai.view.NonScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        createToolbar();
        fabClick();
        createNavDraw();

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

    private void fabClick(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.MaterialDialogSheet);
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
                List<CheckInFunctionInfo> checkInFunctionInfos = new ArrayList<CheckInFunctionInfo>();
                checkInFunctionInfos.add(new CheckInFunctionInfo(R.drawable.ic_menu_camera, "Check in", "30 phut"));
                checkInFunctionInfos.add(new CheckInFunctionInfo(R.drawable.ic_menu_gallery, "Lập đơn hàng", "10 phut"));
                checkInFunctionInfos.add(new CheckInFunctionInfo(R.drawable.ic_menu_send, "Kết thúc", "30 phut"));
                CheckinFunctionAdapter adapter = new CheckinFunctionAdapter(MainActivity.this, checkInFunctionInfos);
                listView.setAdapter(adapter);

                mBottomSheetDialog.setContentView(sheetView);
                mBottomSheetDialog.setCancelable (false);
                mBottomSheetDialog.show();
                fab.setVisibility(View.GONE);
            }
        });
    }

    private void createNavDraw() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
