package com.congtyhai.haidms.manageorders;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.congtyhai.adapter.C2OrderAdapter;
import com.congtyhai.adapter.StaffOrderAdapter;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.order.C2OrderShowSend;
import com.congtyhai.model.api.order.StaffOrderShowSend;
import com.congtyhai.model.api.order.YourOrderInfo;
import com.congtyhai.model.api.order.YourOrderShowResult;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YourOrdersActivity extends BaseActivity {

    int page = 1;
    @BindView(R.id.list)
    LoadMoreListView listView;

    List<YourOrderInfo> yourOrderInfos;

    C2OrderAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_orders);
        createToolbar();

        ButterKnife.bind(this);
        yourOrderInfos = new ArrayList<>();

        mAdapter = new C2OrderAdapter(yourOrderInfos, this);

        listView.setAdapter(mAdapter);

        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                makeRequest();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                YourOrderInfo info = yourOrderInfos.get(i);

                HAIRes.getInstance().yourOrderInfo = info;
                commons.startActivity(YourOrdersActivity.this, YourOrderProductActivity.class);

            }
        });

        makeRequest();

    }
    private void makeRequest() {

        showpDialog();

        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");

        C2OrderShowSend info = new C2OrderShowSend();
        info.setPage(page);

        info.setToken(token);
        info.setUser(user);

        Call<YourOrderShowResult> call = apiInterface().c2OrderShow(info);

        call.enqueue(new Callback<YourOrderShowResult>() {
            @Override
            public void onResponse(Call<YourOrderShowResult> call, Response<YourOrderShowResult> response) {

                if (response.body() != null && response.body().getOrders().size() > 0) {
                    page++;
                    yourOrderInfos.addAll(response.body().getOrders());
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
            public void onFailure(Call<YourOrderShowResult> call, Throwable t) {
                hidepDialog();
            }
        });

    }

}
