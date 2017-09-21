package com.congtyhai.haidms.Agency;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.congtyhai.adapter.AgencyAdapter;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.AgencyInfo;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.DividerItemDecoration;
import com.congtyhai.view.RecyclerTouchListener;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowAgencyActivity extends BaseActivity {

    private List<AgencyInfo> agencyList ;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private AgencyAdapter mAdapter;

    int SHOW_DETAIL_AGENCY = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_agency);
        ButterKnife.bind(this);
        createToolbar();
        agencyList = new ArrayList<>();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commons.startActivity(ShowAgencyActivity.this, AddAgencyActivity.class);
            }
        });

        mAdapter = new AgencyAdapter(agencyList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                HAIRes.getInstance().currentAgencySelect = agencyList.get(position);
                Intent intent = commons.createIntent(ShowAgencyActivity.this, ShowAgencyDetailActivity.class);
                startActivityForResult(intent, SHOW_DETAIL_AGENCY);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


       new ReadDataTask().execute();

    }


    private void makeRequest() {
        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
        Call<AgencyInfo[]> call = apiInterface().getAgencyC2(user, token);
        call.enqueue(new Callback<AgencyInfo[]>() {
            @Override
            public void onResponse(Call<AgencyInfo[]> call, Response<AgencyInfo[]> response) {
               if(response.body() != null) {
                   saveListAgency(response.body());
               }
              hidepDialog();
                new ReadDataTask().execute();
            }

            @Override
            public void onFailure(Call<AgencyInfo[]> call, Throwable t) {
                hidepDialog();
            }
        });
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
            agencyList = new ArrayList<>();
            for (AgencyInfo info : result) {
                agencyList.add(info);
            }

            mAdapter = new AgencyAdapter(agencyList);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

            hidepDialog();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_agency_show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refesh_action:
                makeRequest();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SHOW_DETAIL_AGENCY) {
            if (resultCode == RESULT_OK) {
                makeRequest();
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }



}
