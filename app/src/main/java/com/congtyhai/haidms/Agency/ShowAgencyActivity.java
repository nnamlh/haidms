package com.congtyhai.haidms.Agency;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import com.congtyhai.adapter.AgencyAdapter;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.MainActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.haidms.showinfo.ShowProductActivity;
import com.congtyhai.model.api.AgencyInfo;
import com.congtyhai.model.app.C2Info;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.DividerItemDecoration;
import com.congtyhai.view.RecyclerTouchListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowAgencyActivity extends BaseActivity {

    private List<AgencyInfo> agencyList ;
    private List<AgencyInfo> agencyListTemp ;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private AgencyAdapter mAdapter;

    int SHOW_DETAIL_AGENCY = 1;

    AlertDialog.Builder builderSingle;
    private List<Integer> groups;

    String codeRequest = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_agency);
        ButterKnife.bind(this);
        createToolbar();

        Intent intent = getIntent();
        codeRequest = intent.getStringExtra(HAIRes.getInstance().KEY_INTENT_ACTION);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commons.startActivity(ShowAgencyActivity.this, AddAgencyActivity.class);
            }
        });

        if(!"".equals(codeRequest)) {

            fab.setVisibility(View.GONE);
        }

        agencyList = new ArrayList<>();
        agencyListTemp = new ArrayList<>();

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
                AgencyInfo info =  agencyList.get(position);
                HAIRes.getInstance().currentAgencySelect = agencyList.get(position);
                if ("stafforder".equals(codeRequest)) {
                    Intent intentResult = getIntent();
                    intentResult.putExtra("code", agencyList.get(position).getCode());
                    setResult(Activity.RESULT_OK,intentResult);
                    finish();
                } else if ("createorder".equals(codeRequest)) {
                    HAIRes.getInstance().inOder = 1;
                    HAIRes.getInstance().CurrentAgency = info.getCode();
                    C2Info c2Info = new C2Info();
                    c2Info.setCode(info.getCode());
                    c2Info.setDeputy(info.getDeputy());
                    c2Info.setStore(info.getName());
                    c2Info.setC1(info.getC1());

                    HAIRes.getInstance().c2Select = c2Info;

                    commons.startActivity(ShowAgencyActivity.this, ShowProductActivity.class);
                }
                else {
                    Intent intent = commons.createIntent(ShowAgencyActivity.this, ShowAgencyDetailActivity.class);
                    startActivityForResult(intent, SHOW_DETAIL_AGENCY);
                }


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


       new ReadDataTask().execute();



    }
    private void createDialogGroup() {
        builderSingle = new AlertDialog.Builder(ShowAgencyActivity.this);
        builderSingle.setIcon(R.mipmap.ic_logo);
        builderSingle.setTitle("Chọn cụm:");
        final ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(ShowAgencyActivity.this, android.R.layout.select_dialog_singlechoice, groups);
        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                agencyList.clear();
                int code = groups.get(which);
                for(AgencyInfo info : agencyListTemp) {
                    if(info.getGroup() == code) {
                        agencyList.add(info);
                    }
                }
                mAdapter.notifyDataSetChanged();

            }
        });

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
            groups = new ArrayList<>();
            agencyListTemp = new ArrayList<>();
            for (AgencyInfo info : result) {
                agencyList.add(info);
                agencyListTemp.add(info);
                if(!groups.contains(info.getGroup())) {
                    groups.add(info.getGroup());
                }
            }
            Collections.sort(groups);
            mAdapter = new AgencyAdapter(agencyList);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            createDialogGroup();
            hidepDialog();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_agency_show, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.find_action).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                handleSearch(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refesh_action:
                makeRequest();
                return true;
            case R.id.filter_group:
                builderSingle.show();
                return  true;
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
    private void handleSearch(String query) {
        agencyList.clear();

        for(AgencyInfo info: agencyListTemp) {
            if (info.getCode().contains(query)){
                agencyList.add(info);
            }
        }
        mAdapter.notifyDataSetChanged();
    }


}
