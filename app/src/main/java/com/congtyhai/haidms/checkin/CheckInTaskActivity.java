package com.congtyhai.haidms.checkin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.congtyhai.adapter.TaskAdapter;
import com.congtyhai.haidms.Agency.ShowAgencyActivity;
import com.congtyhai.haidms.Agency.ShowAgencyDetailActivity;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.CheckInTaskResult;
import com.congtyhai.model.api.CheckInTaskSend;
import com.congtyhai.model.api.TaskInfoResult;
import com.congtyhai.model.app.TaskInfo;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.DividerItemDecoration;
import com.congtyhai.view.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckInTaskActivity extends BaseActivity {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    List<TaskInfo> taskInfos;

    TaskAdapter adapter;

    HashMap<String, Integer> mapCodeImage;

    String agencyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_task);
        createToolbar();
        ButterKnife.bind(this);
        createLocation();
        Intent intent = getIntent();
        agencyCode = intent.getStringExtra(HAIRes.getInstance().KEY_INTENT_AGENCY_CODE);
        getSupportActionBar().setTitle("Ghé thăm khách hàng: " + agencyCode);

        taskInfos = new ArrayList<>();
        adapter = new TaskAdapter(taskInfos);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
       // recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
      //  recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        mapCodeImage = new HashMap<>();
        mapCodeImage.put("checkintask", R.mipmap.ic_checkin_task);
        mapCodeImage.put("ordertask", R.mipmap.ic_order_task);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        makeRequest(agencyCode);

    }

    private void makeRequest(String code) {
        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
        CheckInTaskSend info = new CheckInTaskSend();
        info.setUser(user);
        info.setToken(token);
        info.setCode(code);
        Call<CheckInTaskResult> call = apiInterface().checkInTask(info);
        call.enqueue(new Callback<CheckInTaskResult>() {
            @Override
            public void onResponse(Call<CheckInTaskResult> call, Response<CheckInTaskResult> response) {

                if(response.body() != null) {

                    for(TaskInfoResult task: response.body().getTasks()) {
                        TaskInfo taskInfo = new TaskInfo();
                        taskInfo.setCode(task.getCode());
                        taskInfo.setName(task.getName());
                        taskInfo.setTime(task.getTime());

                        if(mapCodeImage.containsKey(taskInfo.getCode())) {
                            taskInfo.setImage(mapCodeImage.get(taskInfo.getCode()));
                        } else {
                            taskInfo.setImage(R.mipmap.ic_checkin_task);
                        }

                        if(taskInfo.getCode().equals("checkintask")) {
                            taskInfo.setTimeRemain(response.body().timeRemain);
                        } else {
                            taskInfo.setTimeRemain(-1);
                        }
                        taskInfos.add(taskInfo);
                    }
                    adapter.notifyDataSetChanged();
                }

                hidepDialog();
            }

            @Override
            public void onFailure(Call<CheckInTaskResult> call, Throwable t) {
                hidepDialog();
            }
        });
    }

}
