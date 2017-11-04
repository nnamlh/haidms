package com.congtyhai.haidms.manageorders;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.congtyhai.adapter.C1OrderProductHistoryAdapter;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.order.C1OrderProductUpdateSend;
import com.congtyhai.model.api.order.OrderProductHistory;
import com.congtyhai.model.api.order.OrderProductResult;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.NonScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YourOrderProductHistoryActivity extends BaseActivity {

    @BindView(R.id.list)
    NonScrollListView listView;

    List<OrderProductHistory> orderProductHistories;

    C1OrderProductHistoryAdapter adapter;

    OrderProductResult orderProductResult;

    String unit;

    @BindView(R.id.ename)
    EditText eName;
    @BindView(R.id.equantity)
    EditText eQuantity;
    @BindView(R.id.efinish)
    EditText eFinish;

    @BindView(R.id.ec1)
    EditText eC1;

    @BindView(R.id.ec1phone)
    EditText eC1Phone;

    @BindView(R.id.ec1address)
    EditText eC1Address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_order_product_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createToolbar();

        ButterKnife.bind(this);

        orderProductResult = HAIRes.getInstance().orderProductResult;

        eName.setText(orderProductResult.getProductName());

        eQuantity.setText(commons.getOrderDetailText(orderProductResult.getQuantityBox(), orderProductResult.getQuantity(),unit ));
        eFinish.setText(commons.getOrderDetailText(orderProductResult.getQuantityBox(), orderProductResult.getQuantityFinish(), unit));

        eC1.setText(orderProductResult.getC1Store());

        eC1Phone.setText(orderProductResult.getC1Phone());

        eC1Address.setText(orderProductResult.getC1Address());

        orderProductHistories = new ArrayList<>();
        adapter = new C1OrderProductHistoryAdapter(this, orderProductHistories);

        listView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + orderProductResult.getC1Phone()));
                if (ActivityCompat.checkSelfPermission(YourOrderProductHistoryActivity.this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                }
            }
        });
        makeRequest();
    }
    private  void makeRequest() {
        orderProductHistories.clear();
        showpDialog();
        Call<List<OrderProductHistory>> call = apiInterface().yourOrderProductHistory(orderProductResult.getOrderId(), orderProductResult.getProductId());

        call.enqueue(new Callback<List<OrderProductHistory>>() {
            @Override
            public void onResponse(Call<List<OrderProductHistory>> call, Response<List<OrderProductHistory>> response) {
                if(response.body() != null) {
                    orderProductHistories.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
                hidepDialog();
            }

            @Override
            public void onFailure(Call<List<OrderProductHistory>> call, Throwable t) {
                commons.showToastDisconnect(getApplicationContext());
                hidepDialog();
            }
        });
    }
}
