package com.congtyhai.haidms.manageorders;

import android.os.Bundle;

import com.congtyhai.adapter.StaffOrderAdapter;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.order.YourOrderInfo;
import com.congtyhai.view.LoadMoreListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YourOrdersActivity extends BaseActivity {

    int page = 1;
    @BindView(R.id.list)
    LoadMoreListView listView;

    List<YourOrderInfo> staffOrderInfos;

    StaffOrderAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_orders);
        createToolbar();

        ButterKnife.bind(this);
    }

}
