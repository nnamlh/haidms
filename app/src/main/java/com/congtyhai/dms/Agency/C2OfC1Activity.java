package com.congtyhai.dms.Agency;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.congtyhai.adapter.C2C1Adapter;
import com.congtyhai.dms.BaseActivity;
import com.congtyhai.dms.R;
import com.congtyhai.model.api.C2C1Info;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class C2OfC1Activity extends BaseActivity {

    private List<C2C1Info> agencyList ;
    private List<C2C1Info> agencyListTemp ;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private C2C1Adapter mAdapter;

    boolean isResult = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c2_of_c1);
        createToolbar();
        ButterKnife.bind(this);
        agencyList = new ArrayList<>();
        agencyListTemp = new ArrayList<>();

        mAdapter = new C2C1Adapter(agencyList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                C2C1Info info = agencyList.get(position);
                if (isResult) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("code", info.getCode());
                    returnIntent.putExtra("name", info.getName());
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        Intent intent = getIntent();

        String code = intent.getStringExtra(HAIRes.getInstance().KEY_INTENT_TEMP);

        if("getcode".equals(code)) {
            isResult = true;
        }


        new ReadDataTask().execute();


    }


    private void makeRequest() {

        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
        Call<C2C1Info[]> call = apiInterface().getC2C1(user, token);
        call.enqueue(new Callback<C2C1Info[]>() {
            @Override
            public void onResponse(Call<C2C1Info[]> call, Response<C2C1Info[]> response) {
                if(response.body() != null) {
                    saveListC2OfC1(response.body());
                }
                hidepDialog();
                new ReadDataTask().execute();
            }

            @Override
            public void onFailure(Call<C2C1Info[]> call, Throwable t) {
                hidepDialog();
            }
        });
    }

    private class ReadDataTask extends AsyncTask<String, Integer, List<C2C1Info>> {
        protected List<C2C1Info> doInBackground(String... urls) {

            List<C2C1Info> data = new ArrayList<>();
            try {

                data = getListC2C1();

            } catch (Exception e) {

            }
            return data;
        }

        @Override
        protected void onPreExecute() {
            showpDialog();
        }

        protected void onPostExecute(List<C2C1Info> result) {
            agencyList = new ArrayList<>();
            agencyListTemp = new ArrayList<>();
            for (C2C1Info info : result) {
                agencyList.add(info);
                agencyListTemp.add(info);
            }

            mAdapter = new C2C1Adapter(agencyList);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            hidepDialog();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleSearch(String query) {
        agencyList.clear();

        for(C2C1Info info: agencyListTemp) {
            if (info.getCode().contains(query)){
                agencyList.add(info);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

}
