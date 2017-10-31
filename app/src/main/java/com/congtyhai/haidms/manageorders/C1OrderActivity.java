package com.congtyhai.haidms.manageorders;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.congtyhai.adapter.C1OrderAdapter;
import com.congtyhai.adapter.C2C1Adapter;
import com.congtyhai.haidms.Agency.C2OfC1Activity;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.C2C1Info;
import com.congtyhai.model.api.order.C1OrderInfo;
import com.congtyhai.model.api.order.C1OrderShowResult;
import com.congtyhai.model.api.order.C1OrderShowSend;
import com.congtyhai.util.EndlessRecyclerViewScrollListener;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class C1OrderActivity extends BaseActivity {

    @BindView(R.id.estatus)
    Spinner eType;
    @BindView(R.id.ec2)
    TextView eC2;

    String[]  listTypeName = {"Đang thực hiện", "Đã thực hiện"};

    String[] listTypeId = {"process", "finish"};

    int page = 0;

    AlertDialog.Builder builderSingle;

    String[] lC2Choose = {"Tất cả", "Khác"};

    String c2Choose = "";

    int GET_C2_CODE = 2;

    String typeChoose = "process";

    EndlessRecyclerViewScrollListener scrollListener;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    List<C1OrderInfo> c1OrderInfos;

    C1OrderAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c1_order);
        createToolbar();
        ButterKnife.bind(this);

        initTypeOrder();

        createDialogGroup();

        eC2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builderSingle.show();
            }
        });

        c1OrderInfos = new ArrayList<>();
        mAdapter = new C1OrderAdapter(c1OrderInfos);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager  = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                makeRequest();
            }
        };
        // Adds the scroll listener to RecyclerView
        recyclerView.addOnScrollListener(scrollListener);

        makeRequest();

    }

    private void makeRequest() {
        page++;
        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");

        C1OrderShowSend info = new C1OrderShowSend();
        info.setPage(page);
        info.setC2Code(c2Choose);
        info.setStatus(typeChoose);
        info.setToken(token);
        info.setUser(user);

        Call<C1OrderShowResult> call = apiInterface().c1OrderShow(info);

        call.enqueue(new Callback<C1OrderShowResult>() {
            @Override
            public void onResponse(Call<C1OrderShowResult> call, Response<C1OrderShowResult> response) {

                //
                if (response.body() != null) {

                    c1OrderInfos.addAll(response.body().getOrders());

                    mAdapter.notifyDataSetChanged();

                }

                hidepDialog();
            }

            @Override
            public void onFailure(Call<C1OrderShowResult> call, Throwable t) {
                hidepDialog();
            }
        });

    }

    private void initTypeOrder() {

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(C1OrderActivity.this, android.R.layout.simple_spinner_item,listTypeName );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eType.setAdapter(adapter);

        eType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                typeChoose = listTypeId[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void createDialogGroup() {
        builderSingle = new AlertDialog.Builder(C1OrderActivity.this);
        builderSingle.setIcon(R.mipmap.ic_logo);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(C1OrderActivity.this, android.R.layout.select_dialog_singlechoice, lC2Choose);
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
                    eC2.setText("Tất cả");
                    if (!"".equals(c2Choose)) {
                        c2Choose = "";
                        page = 0;
                        makeRequest();

                    }

                } else {
                    Intent intent = commons.createIntent(C1OrderActivity.this, C2OfC1Activity.class);
                    intent.putExtra(HAIRes.getInstance().KEY_INTENT_TEMP, "getcode");
                    startActivityForResult(intent, GET_C2_CODE);
                }

            }
        });

    }
}
