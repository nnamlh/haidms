package com.congtyhai.haidms.manageorders;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.congtyhai.adapter.C1OrderProductHistoryAdapter;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.ResultInfo;
import com.congtyhai.model.api.order.OrderProductHistory;
import com.congtyhai.model.api.order.OrderProductResult;
import com.congtyhai.model.api.order.OrderProductUpdateSend;
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
    int quantityOrder;
    boolean isUpdate = false;

    int quantityFinish;
    int quantityBox;
    @BindView(R.id.ename)
    EditText eName;
    @BindView(R.id.equantity)
    EditText eQuantity;
    @BindView(R.id.efinish)
    EditText eFinish;
    OrderProductUpdateSend infoSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_order_product_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createToolbar();

        ButterKnife.bind(this);

        orderProductResult = HAIRes.getInstance().orderProductResult;

        quantityBox = orderProductResult.getQuantityBox();
        quantityFinish = orderProductResult.getQuantityFinish();
        quantityOrder = orderProductResult.getQuantity();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");

        infoSend = new OrderProductUpdateSend();
        infoSend.setOrderId(orderProductResult.getOrderId());
        infoSend.setProductId(orderProductResult.getProductId());
        infoSend.setToken(token);
        infoSend.setUser(user);

        eName.setText(orderProductResult.getProductName());

        eQuantity.setText(commons.getOrderDetailText(quantityBox, quantityOrder, unit));
        eFinish.setText(commons.getOrderDetailText(quantityBox, quantityFinish, unit));


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
                if (TextUtils.isEmpty(eCan.getText().toString())) {
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
                if (TextUtils.isEmpty(eBox.getText().toString())) {
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
                    try {
                        int can = Integer.parseInt(eCan.getText().toString());
                        int box = Integer.parseInt(eBox.getText().toString());

                        int sum = box + quantityBox * can;

                        infoSend.setQuantity(sum);

                        makeUpdate();

                    } catch (Exception e) {

                    }
                } else {
                    commons.makeToast(YourOrderProductHistoryActivity.this, "Nhập số lượng").show();
                }

            }
        });

        builder.show();
    }

    private void makeRequest() {
        orderProductHistories.clear();
        showpDialog();
        Call<List<OrderProductHistory>> call = apiInterface().yourOrderProductHistory(orderProductResult.getOrderId(), orderProductResult.getProductId());

        call.enqueue(new Callback<List<OrderProductHistory>>() {
            @Override
            public void onResponse(Call<List<OrderProductHistory>> call, Response<List<OrderProductHistory>> response) {
                if (response.body() != null) {
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

    private void makeUpdate() {
        showpDialog();
        Call<ResultInfo> call = apiInterface().orderProductUpdate(infoSend);
        call.enqueue(new Callback<ResultInfo>() {
            @Override
            public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                hidepDialog();
                if (response.body() != null) {
                    if (response.body().getId().equals("1")) {
                        commons.makeToast(YourOrderProductHistoryActivity.this, "Cập nhật thành công").show();
                        isUpdate = true;
                        quantityFinish += infoSend.getQuantity();
                        eFinish.setText(commons.getOrderDetailText(quantityBox, quantityFinish, unit));
                        makeRequest();
                    } else {
                        commons.showAlertInfo(YourOrderProductHistoryActivity.this, "Cảnh báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
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
                commons.showToastDisconnect(YourOrderProductHistoryActivity.this);
            }
        });
    }
}
