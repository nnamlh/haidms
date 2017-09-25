package com.congtyhai.haidms.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.congtyhai.adapter.ProductOrderAdapter;
import com.congtyhai.haidms.Agency.AddAgencyActivity;
import com.congtyhai.haidms.Agency.ShowAgencyActivity;
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
}
