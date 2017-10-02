package com.congtyhai.haidms.Event;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.congtyhai.adapter.EventAdapter;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.AuthInfo;
import com.congtyhai.model.api.ResultEvent;
import com.congtyhai.model.api.ResultEventInfo;
import com.congtyhai.util.ApiInterface;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.RecyclerTouchListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    EventAdapter adapter;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        createToolbar();
        ButterKnife.bind(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        HAIRes.getInstance().clearListEvent();
        adapter = new EventAdapter(this, HAIRes.getInstance().getResultEventInfos());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ResultEventInfo info = HAIRes.getInstance().getResultEventInfos().get(position);

                Intent i = new Intent(EventActivity.this, EventDetailActivity.class);

                i.putExtra("EventID", info.getEid());

                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        getInfo();
                                    }
                                }
        );
    }

    @Override
    public void onRefresh() {
        HAIRes.getInstance().clearListEvent();
        adapter = new EventAdapter(this, HAIRes.getInstance().getResultEventInfos());
        recyclerView.setAdapter(adapter);
        getInfo();
    }

    private void getInfo() {

        // showDialog();
        swipeRefreshLayout.setRefreshing(true);
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
        AuthInfo info = new AuthInfo(user, token);

        Call<ResultEvent> call = apiInterface().loyaltyEvent(info);

        call.enqueue(new Callback<ResultEvent>() {
            @Override
            public void onResponse(Call<ResultEvent> call,
                                   Response<ResultEvent> response) {

                if (response.body() != null) {
                    if ("1".equals(response.body().getId())) {

                        for (ResultEventInfo info : response.body().getEvents()) {
                            HAIRes.getInstance().addListEvent(info);
                        }

                        adapter.notifyDataSetChanged();

                    } else {
                        commons.showAlertInfo(EventActivity.this, "Cảnh báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    }
                }

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ResultEvent> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                commons.showAlertInfo(EventActivity.this, "Cảnh báo", "Đường truyền bị lỗi, kiểm tra lại kết nối wifi hoặc 3G.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }
        });
    }
}
