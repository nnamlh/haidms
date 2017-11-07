package com.congtyhai.haidms.manageorders;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.congtyhai.adapter.C1OrderProductAdapter;
import com.congtyhai.adapter.YourOrderProductAdapter;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.order.C1OrderInfo;
import com.congtyhai.model.api.order.OrderProductResult;
import com.congtyhai.model.api.order.YourOrderInfo;
import com.congtyhai.util.HAIRes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YourOrderProductActivity extends BaseActivity {

    @BindView(R.id.ecode)
    EditText eCode;
    @BindView(R.id.estore)
    EditText eStore;
    @BindView(R.id.ephone)
    EditText ePhone;
    @BindView(R.id.eaddress)
    EditText eAddress;
    @BindView(R.id.list)
    ListView listView;

    YourOrderProductAdapter adapter;

    List<OrderProductResult> orderProducts;

    YourOrderInfo info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_order_product);
        createToolbar();
        ButterKnife.bind(this);

        info = HAIRes.getInstance().yourOrderInfo;

        eCode.setText(info.getCode());

        eStore.setText(info.getC2Name() + " ( " + info.getC2Code() + " )");

        eAddress.setText(info.getAddress());

        ePhone.setText(info.getPhone());

        orderProducts = new ArrayList<>();

        adapter = new YourOrderProductAdapter(orderProducts, this);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OrderProductResult info = orderProducts.get(i);
                Intent intent = commons.createIntent(YourOrderProductActivity.this, YourOrderProductHistoryActivity.class);
                HAIRes.getInstance().orderProductResult = info;
                startActivity(intent);
            }
        });

        makeRequest();

    }
    private void makeRequest(){

        orderProducts.clear();
        showpDialog();

        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");

        Call<List<OrderProductResult>> call = apiInterface().yourOrderProduct(user, info.getOrderId());

        call.enqueue(new Callback<List<OrderProductResult>>() {
            @Override
            public void onResponse(Call<List<OrderProductResult>> call, Response<List<OrderProductResult>> response) {
                if (response.body() != null) {
                    orderProducts.addAll(response.body());

                    adapter.notifyDataSetChanged();
                }

                hidepDialog();
            }

            @Override
            public void onFailure(Call<List<OrderProductResult>> call, Throwable t) {
                commons.showToastDisconnect(getApplicationContext());
                hidepDialog();
            }
        });

    }

}
