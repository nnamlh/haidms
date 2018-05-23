package com.congtyhai.dms.showinfo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.congtyhai.adapter.BranchAdapter;
import com.congtyhai.dms.BaseActivity;
import com.congtyhai.dms.R;
import com.congtyhai.model.api.BranchInfoResult;
import com.congtyhai.util.ItemRowClick;
import com.congtyhai.view.DividerItemDecoration;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowBranchActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<BranchInfoResult> branchInfoResults;
    BranchAdapter mAdapter;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_branch);
        createToolbar();
        ButterKnife.bind(this);

        branchInfoResults = new ArrayList<>();

        mAdapter = new BranchAdapter(branchInfoResults, new ItemRowClick() {
            @Override
            public void onClick(int position) {
                BranchInfoResult infoResult = branchInfoResults.get(position);
                LatLng me = new LatLng(infoResult.getLat(), infoResult.getLng());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(me));
            }
        }, new ItemRowClick() {
            @Override
            public void onClick(int position) {
                BranchInfoResult infoResult = branchInfoResults.get(position);
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + infoResult.getPhone()));
                if (ActivityCompat.checkSelfPermission(ShowBranchActivity.this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        makeRequest();

    }

    private void makeRequest() {

        showpDialog();

        Call<List<BranchInfoResult>> call = apiInterface().getBranch();
        call.enqueue(new Callback<List<BranchInfoResult>>() {
            @Override
            public void onResponse(Call<List<BranchInfoResult>> call, Response<List<BranchInfoResult>> response) {
                if(response.body() != null) {
                    branchInfoResults.addAll(response.body());
                    for(BranchInfoResult infoResult : response.body()) {
                        LatLng me = new LatLng(infoResult.getLat(), infoResult.getLng());
                        mMap.addMarker(new MarkerOptions().position(me).title(infoResult.getName()).snippet(infoResult.getAddress()));
                    }

                    mAdapter.notifyDataSetChanged();
                }
                hidepDialog();
            }

            @Override
            public void onFailure(Call<List<BranchInfoResult>> call, Throwable t) {
                hidepDialog();
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(7.0f);
        mMap.setMaxZoomPreference(16.0f);
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
                distance.setText(d + " m");
                return v;
            }
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLng(me));
    }
}
