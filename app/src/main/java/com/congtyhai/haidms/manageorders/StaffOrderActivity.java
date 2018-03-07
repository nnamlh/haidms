package com.congtyhai.haidms.manageorders;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.congtyhai.adapter.StaffOrderAdapter;
import com.congtyhai.haidms.Agency.ShowAgencyActivity;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.order.StaffOrderShowSend;
import com.congtyhai.model.api.order.YourOrderInfo;
import com.congtyhai.model.api.order.YourOrderShowResult;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.LoadMoreListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffOrderActivity extends BaseActivity {

    int page = 1;

    AlertDialog.Builder builderSingle;

    String[] lC2Choose = {"Tất cả", "Khác"};

    String c2Choose = "";

    int GET_C2_CODE = 2;

    int CREATE_ORDER = 3;

    @BindView(R.id.list)
    LoadMoreListView listView;

    List<YourOrderInfo> staffOrderInfos;

    StaffOrderAdapter mAdapter;

    final int FILTER_ACTION =11;

    String fDate = "";
    String tDate = "";
    String c1Code = "";
    String status = "";
    String place = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_order);
        createToolbar();
        ButterKnife.bind(this);
        HAIRes.getInstance().clearProductOrder();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = commons.createIntent(StaffOrderActivity.this, ShowAgencyActivity.class);
                intent.putExtra(HAIRes.getInstance().KEY_INTENT_ACTION, "createorder");
                startActivityForResult(intent, CREATE_ORDER);
            }
        });

        createDialogGroup();
        //
        staffOrderInfos = new ArrayList<>();

        mAdapter = new StaffOrderAdapter(staffOrderInfos, this);

        listView.setAdapter(mAdapter);

        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                makeRequest();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                YourOrderInfo info = staffOrderInfos.get(i);

                HAIRes.getInstance().yourOrderInfo = info;
                HAIRes.getInstance().CREATE_ORDER_TYPE = 0;
                commons.startActivity(StaffOrderActivity.this, YourOrderProductActivity.class);

            }
        });
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int showMonth = calendar.get(Calendar.MONTH) + 1;
        int showYear = calendar.get(Calendar.YEAR);

        int days = countDayInMonth(showYear, showMonth);

        fDate = "01/" + showMonth + "/" + showYear;
        tDate  = days + "/" + showMonth + "/" + showYear;

        makeRequest();
    }

    private void makeRequest() {

        showpDialog();

        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");


        StaffOrderShowSend info = new StaffOrderShowSend();
        info.setPage(page);
        info.setC1Code(c1Code);
        info.setFdate(fDate);
        info.setTdate(tDate);
        info.setPlace(place);
        info.setStatus(status);
        info.setToken(token);
        info.setUser(user);

        Call<YourOrderShowResult> call = apiInterface().staffOrderShow(info);

        call.enqueue(new Callback<YourOrderShowResult>() {
            @Override
            public void onResponse(Call<YourOrderShowResult> call, Response<YourOrderShowResult> response) {

                if (response.body() != null && response.body().getOrders().size() > 0) {
                    page++;
                    staffOrderInfos.addAll(response.body().getOrders());
                    mAdapter.notifyDataSetChanged();
                    listView.onLoadMoreComplete();
                } else {
                    mAdapter.notifyDataSetChanged();
                    listView.onLoadMoreComplete();
                    listView.setOnLoadMoreListener(null);
                }
                hidepDialog();
            }

            @Override
            public void onFailure(Call<YourOrderShowResult> call, Throwable t) {
                hidepDialog();
            }
        });

    }


    private void createDialogGroup() {
        builderSingle = new AlertDialog.Builder(StaffOrderActivity.this);
        builderSingle.setIcon(R.mipmap.ic_logo);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(StaffOrderActivity.this, android.R.layout.select_dialog_singlechoice, lC2Choose);
        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (!c2Choose.equals("")) {

                    }

                    c2Choose = "";
                    page = 1;
                    staffOrderInfos.clear();
                    makeRequest();

                } else {
                    Intent intent = commons.createIntent(StaffOrderActivity.this, ShowAgencyActivity.class);
                    intent.putExtra(HAIRes.getInstance().KEY_INTENT_ACTION, "stafforder");
                    startActivityForResult(intent, GET_C2_CODE);
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILTER_ACTION) {
            if (resultCode == Activity.RESULT_OK) {

                c1Code = data.getStringExtra("c1Code");
                status = data.getStringExtra("status");
                place = data.getStringExtra("place");
                fDate =data.getStringExtra("fDate");
                tDate =data.getStringExtra("tDate");
                page = 1;
                staffOrderInfos.clear();
                mAdapter.notifyDataSetChanged();
                makeRequest();

            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }

        if(requestCode == CREATE_ORDER) {

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.filter_action:
                Intent intent = commons.createIntent(StaffOrderActivity.this, FilterActivity.class);
                startActivityForResult(intent, FILTER_ACTION);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
