package com.congtyhai.haidms.manageorders;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.congtyhai.adapter.C1OrderProductHistoryAdapter;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.ResultInfo;
import com.congtyhai.model.api.order.OrderProductHistory;
import com.congtyhai.model.api.order.OrderProductUpdateSend;
import com.congtyhai.util.HAIRes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class C1OrderProductHistoryActivity extends BaseActivity {


    String orderId;

    String productId;

    @BindView(R.id.list)
    ListView listView;

    List<OrderProductHistory> orderProductHistories;

    C1OrderProductHistoryAdapter adapter;




    OrderProductUpdateSend infoSend;

    int quantityOrder;
    boolean isUpdate = false;

    int quantityFinish;
    int quantityBox;
    String unit;

    @BindView(R.id.ename)
    EditText eName;
    @BindView(R.id.equantity)
    EditText eQuantity;
    @BindView(R.id.efinish)
    EditText eFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c1_order_product_history);
        createToolbar();

        ButterKnife.bind(this);

        infoSend = new OrderProductUpdateSend();
        Intent intent = getIntent();

        orderId = intent.getStringExtra("orderId");

        productId = intent.getStringExtra("productId");

        quantityBox = intent.getIntExtra("quantityBox", 0);

        quantityOrder = intent.getIntExtra("quantityOrder", 0);

        quantityFinish = intent.getIntExtra("quantityFinish", 0);

        String product = intent.getStringExtra("product");

        unit = intent.getStringExtra("unit");

        eName.setText(product);

        eQuantity.setText(commons.getOrderDetailText(quantityBox, quantityOrder,unit ));
        eFinish.setText(commons.getOrderDetailText(quantityBox, quantityFinish, unit));


        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");


        infoSend.setOrderId(orderId);
        infoSend.setProductId(productId);
        infoSend.setToken(token);
        infoSend.setUser(user);

        orderProductHistories = new ArrayList<>();
        adapter = new C1OrderProductHistoryAdapter(this, orderProductHistories);

        listView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeQuantity();
            }
        });
        makeRequest();
    }

    private void makeUpdate() {
        showpDialog();
        Call<ResultInfo> call = apiInterface().c1OrderProductUpdate(infoSend);
        call.enqueue(new Callback<ResultInfo>() {
            @Override
            public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                hidepDialog();
                if(response.body() != null) {
                    if (response.body().getId().equals("1")){
                        commons.makeToast(C1OrderProductHistoryActivity.this, "Cập nhật thành công").show();
                        isUpdate = true;
                        quantityFinish+=infoSend.getQuantity();
                        eFinish.setText(commons.getOrderDetailText(quantityBox, quantityFinish, unit));
                        makeRequest();
                    }else {
                        commons.showAlertInfo(C1OrderProductHistoryActivity.this, "Cảnh báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultInfo> call, Throwable t) {
                hidepDialog();
                commons.showToastDisconnect(C1OrderProductHistoryActivity.this);
            }
        });
    }

    public void changeQuantity() {

        View viewDialog = this.getLayoutInflater().inflate(R.layout.dialog_c1_order_product_update, null);
        final EditText eCan = (EditText) viewDialog.findViewById(R.id.ecan);
       // eCan.setText("" + countCan);
        final EditText eBox = (EditText) viewDialog.findViewById(R.id.ebox);


        eCan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(eCan.getText().toString())) {
                    eCan.setText("0");
                }
            }
        });

        eBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(eBox.getText().toString())) {
                    eBox.setText("0");
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thao tác");
        builder.setMessage("Nhập số lượng giao hàng");
        builder.setIcon(R.mipmap.ic_logo);
        builder.setView(viewDialog);
        builder.setNegativeButton("Thôi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setPositiveButton("Nhập", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!TextUtils.isEmpty(eCan.getText().toString()) && !TextUtils.isEmpty(eBox.getText().toString())) {
                    try{
                        int can  = Integer.parseInt(eCan.getText().toString());
                        int box = Integer.parseInt(eBox.getText().toString());

                        int sum = box + quantityBox*can;
                        infoSend.setQuantity(sum);

                        makeUpdate();

                    }catch (Exception e) {

                    }
                }else {
                    commons.makeToast(C1OrderProductHistoryActivity.this, "Nhập số lượng").show();
                }

            }
        });

        builder.show();
    }

    @Override
    public void onBackPressed() {
       if(isUpdate) {
           setResult(Activity.RESULT_OK);
           finish();
       } else {
           super.onBackPressed();
       }
    }

    private  void makeRequest() {
        orderProductHistories.clear();
        showpDialog();
        Call<List<OrderProductHistory>> call = apiInterface().c1OrderProductHistory(orderId, productId);

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
