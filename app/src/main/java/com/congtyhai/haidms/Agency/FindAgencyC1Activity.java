package com.congtyhai.haidms.Agency;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.congtyhai.adapter.AgencyC1Adapter;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.AgencyC1Info;
import com.congtyhai.view.DividerItemDecoration;
import com.congtyhai.view.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FindAgencyC1Activity extends BaseActivity {

    private List<AgencyC1Info> agencyList = new ArrayList<>();
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private AgencyC1Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_agency_c1);
        createToolbar();
        ButterKnife.bind(this);


        mAdapter = new AgencyC1Adapter(agencyList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final AgencyC1Info info = agencyList.get(position);
                commons.showAlertCancel(FindAgencyC1Activity.this, "Cảnh báo", "Bạn đang chọn đại lý: " + info.getDeputy() + " - " + info.getCode(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result", info.getCode());
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                });
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        new ReadDataTask().execute();

    }

    private class ReadDataTask extends AsyncTask<String, Integer, List<AgencyC1Info>> {
        protected List<AgencyC1Info> doInBackground(String... urls) {

            List<AgencyC1Info> data = new ArrayList<>();
            try {

                data = getListAgencyC1();

            } catch (Exception e) {

            }
            return data;
        }

        @Override
        protected void onPreExecute() {
            showpDialog();
        }

        protected void onPostExecute(List<AgencyC1Info> result) {
            for (AgencyC1Info info : result) {
                agencyList.add(info);
            }

            mAdapter.notifyDataSetChanged();

            hidepDialog();
        }
    }

}
