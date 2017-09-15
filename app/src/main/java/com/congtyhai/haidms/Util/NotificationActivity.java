package com.congtyhai.haidms.Util;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.congtyhai.adapter.NotificationAdapter;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.NotificationInfo;
import com.congtyhai.model.api.NotificationInfoResult;
import com.congtyhai.util.EndlessRecyclerViewScrollListener;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.DividerItemDecoration;
import com.congtyhai.view.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    NotificationAdapter adapter;
    List<NotificationInfo> notificationInfos;
    EndlessRecyclerViewScrollListener scrollListener;

    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        createToolbar();
        ButterKnife.bind(this);

        notificationInfos = new ArrayList<>();
        adapter = new NotificationAdapter(notificationInfos, this);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager  = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                NotificationInfo info = notificationInfos.get(position);

                Intent intent = commons.createIntent(NotificationActivity.this, NotificationDetailActivity.class);
                intent.putExtra("ID", info.getId());
                intent.putExtra("CONTENT", info.getContent());
                intent.putExtra("READ", info.getIsRead());
                startActivity(intent);
                notificationInfos.get(position).setIsRead(1);
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                makeRequest();
            }
        };
        // Adds the scroll listener to RecyclerView
        recyclerView.addOnScrollListener(scrollListener);
        makeRequest();
    }


    void makeRequest() {
        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
        Call<NotificationInfoResult> call = apiInterface().getNotifications(user, token, page);

        call.enqueue(new Callback<NotificationInfoResult>() {
            @Override
            public void onResponse(Call<NotificationInfoResult> call, Response<NotificationInfoResult> response) {
                if(response.body() != null) {
                    if (response.body().getData() != null && response.body().getData().size() > 0) {
                        page++;
                        for(NotificationInfo info : response.body().getData()) {
                            notificationInfos.add(info);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                hidepDialog();
            }

            @Override
            public void onFailure(Call<NotificationInfoResult> call, Throwable t) {
                hidepDialog();
                commons.makeToast(NotificationActivity.this, HAIRes.getInstance().ANNOUNCEMENT_DISCONNECT_NETWORK);
            }
        });
    }
}
