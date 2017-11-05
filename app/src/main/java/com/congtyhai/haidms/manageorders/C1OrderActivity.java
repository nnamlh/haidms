package com.congtyhai.haidms.manageorders;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.congtyhai.adapter.C1OrderAdapter;
import com.congtyhai.haidms.Agency.C2OfC1Activity;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.order.C1OrderInfo;
import com.congtyhai.model.api.order.C1OrderShowResult;
import com.congtyhai.model.api.order.C1OrderShowSend;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.DividerItemDecoration;
import com.congtyhai.view.LoadMoreListView;

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

    String[] listTypeName = {"Đang thực hiện", "Đã thực hiện"};

    String[] listTypeId = {"process", "finish"};

    int page = 1;

    AlertDialog.Builder builderSingle;

    String[] lC2Choose = {"Tất cả", "Khác"};

    String c2Choose = "";

    int GET_C2_CODE = 2;

    int RESULT_PRODUCT = 3;

    String typeChoose = "process";

    @BindView(R.id.list)
    LoadMoreListView listView;

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
        mAdapter = new C1OrderAdapter(c1OrderInfos, this);
        listView.setAdapter(mAdapter);

        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                makeRequest();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                C1OrderInfo info = c1OrderInfos.get(position);

                HAIRes.getInstance().C1OrderInfo = info;
                Intent intent = commons.createIntent(C1OrderActivity.this, C1OrderProductActivity.class);
                startActivityForResult(intent, RESULT_PRODUCT);
            }
        });

        makeRequest();

    }

    @Override
    protected void onStop() {
        super.onStop();
        HAIRes.getInstance().C1OrderInfo = null;
    }

    public void makeRequest(View view) {
        makeRequest();
    }

    private void makeRequest() {

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

                if (response.body() != null && response.body().getOrders().size() > 0) {
                    page++;
                    c1OrderInfos.addAll(response.body().getOrders());

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
            public void onFailure(Call<C1OrderShowResult> call, Throwable t) {
                hidepDialog();
            }
        });

    }

    private void initTypeOrder() {

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(C1OrderActivity.this, android.R.layout.simple_spinner_item, listTypeName);
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
                        page = 1;
                        c1OrderInfos.clear();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (requestCode == GET_C2_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String code = data.getStringExtra("code");
                String name = data.getStringExtra("name");
                eC2.setText(name);
                c2Choose = code;
                c1OrderInfos.clear();
                makeRequest();
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

}
