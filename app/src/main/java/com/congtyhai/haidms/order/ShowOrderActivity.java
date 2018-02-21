package com.congtyhai.haidms.order;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.congtyhai.adapter.ProductOrderAdapter;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
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

   // int indexSelect = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);
        createToolbar();
        ButterKnife.bind(this);

        agencyCode = HAIRes.getInstance().c2Select.getCode();

        getSupportActionBar().setTitle("Đơn hàng của: " + HAIRes.getInstance().c2Select.getStore());

        adapter = new ProductOrderAdapter(this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        resetMoneyAll();
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

    @Override
    public void onResume() {
        super.onResume();

        notifyAdapter();
        resetMoneyAll();
    }


    public void orderClick(View view) {
       if (HAIRes.getInstance().getProductOrder() == null || HAIRes.getInstance().getProductOrder().size() == 0) {
            commons.makeToast(ShowOrderActivity.this, "Chọn mặt hàng cần đặt").show();
       } else {
           commons.showAlertCancel( ShowOrderActivity.this,"Cảnh báo", "Kiểm tra các mặt hàng và số lượng trước khi đặt hàng", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   commons.startActivity(ShowOrderActivity.this, CompleteOrderActivity.class);
               }
           });
       }
    }

}
