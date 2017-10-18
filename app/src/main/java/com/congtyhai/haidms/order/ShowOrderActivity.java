package com.congtyhai.haidms.order;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.congtyhai.adapter.OrderShowC1Adaper;
import com.congtyhai.adapter.ProductOrderAdapter;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.AgencyC2C1;
import com.congtyhai.model.api.ProductOrder;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.DividerItemDecoration;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowOrderActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    ProductOrderAdapter adapter;

    @BindView(R.id.txtmoney)
    TextView txtMoney;

    String agencyCode;

    int C1_CODE = 2;

    int indexSelect = -1;

    AlertDialog.Builder c1Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);
        createToolbar();
        ButterKnife.bind(this);

        agencyCode = HAIRes.getInstance().CurrentAgency;
        getSupportActionBar().setTitle("Đơn hàng của: " + agencyCode);

        adapter = new ProductOrderAdapter(this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        resetMoneyAll();
        createDialogC1();

    }


    private void createDialogC1() {
        c1Dialog = new AlertDialog.Builder(ShowOrderActivity.this);
        c1Dialog.setIcon(R.mipmap.ic_logo);
        c1Dialog.setTitle("Chọn nơi bán");
        final OrderShowC1Adaper arrayAdapter = new OrderShowC1Adaper(ShowOrderActivity.this);
        c1Dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        c1Dialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AgencyC2C1 agencyC2C1 = HAIRes.getInstance().c2Select.findC1(which);
                HAIRes.getInstance().getProductOrder().get(indexSelect).setC1Name(agencyC2C1.getStore());
                HAIRes.getInstance().getProductOrder().get(indexSelect).setC1Code(agencyC2C1.getCode());
                adapter.notifyDataSetChanged();
            }
        });



    }


    public void changeQuantity(int quantity, final int boxNumber, final int position) {

        int countCan = quantity / boxNumber;
        int countBox = quantity - countCan*boxNumber;

        View viewDialog = ShowOrderActivity.this.getLayoutInflater().inflate(R.layout.dialog_change_quantity_order, null);
        final EditText eCan = (EditText) viewDialog.findViewById(R.id.ecan);
        eCan.setText("" + countCan);
        final EditText eBox = (EditText) viewDialog.findViewById(R.id.ebox);
        eBox.setText("" + countBox);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thao tác");
        builder.setMessage("Thay đổi số lượng mua");
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
                        int quantityCan  = Integer.parseInt(eCan.getText().toString());
                        int quantityBox = Integer.parseInt(eBox.getText().toString());
                        int quantity = quantityBox + boxNumber*quantityCan;
                        HAIRes.getInstance().getProductOrder().get(position).setQuantity(quantity);
                        notifyAdapter();
                        resetMoneyAll();
                    }catch (Exception e) {

                    }
                }else {
                    commons.makeToast(ShowOrderActivity.this, "Nhập số lượng").show();
                }

            }
        });

        builder.show();
    }

    public void changeC1(int position) {
        indexSelect = position;
        c1Dialog.show();
    }

    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }


    public void resetMoneyAll() {
        double price = 0;
        for(ProductOrder order: HAIRes.getInstance().getProductOrder()) {
            price+= order.getPrice() * order.getQuantity();
        }
        txtMoney.setText(HAIRes.getInstance().formatMoneyToText(price));
    }

    public void orderClick(View view) {
        commons.startActivity(ShowOrderActivity.this, CompleteOrderActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == C1_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String code = data.getStringExtra("result");
                String name = data.getStringExtra("name");

            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }



}
