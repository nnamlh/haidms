package com.congtyhai.dms.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.congtyhai.adapter.TaskAdapter;
import com.congtyhai.dms.BaseActivity;
import com.congtyhai.dms.Event.EventSendActivity;
import com.congtyhai.dms.R;
import com.congtyhai.model.app.TaskInfo;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductTaskActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    List<TaskInfo> taskInfos;

    TaskAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_task);
        createToolbar();

        ButterKnife.bind(this);
        taskInfos = new ArrayList<>();
        adapter = new TaskAdapter(taskInfos);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        // recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                TaskInfo info = taskInfos.get(position);
                Intent intent = null;
                switch (info.getCode()) {

                    case "importproduct":
                        intent= commons.createIntent(ProductTaskActivity.this,ProductManageActivity.class);
                        intent.putExtra("STATUS", HAIRes.getInstance().PRODUCT_IMPORT);
                        break;
                    case "exportproduct":
                        intent=commons.createIntent(ProductTaskActivity.this,ProductManageActivity.class);
                        intent.putExtra("STATUS", HAIRes.getInstance().PRODUCT_EXPORT);
                        break;
                    case "transport":
                        intent= commons.createIntent(ProductTaskActivity.this,ProductManageActivity.class);
                        intent.putExtra("STATUS", HAIRes.getInstance().PRODUCT_TRANSPORT);
                        break;
                    case "staffimportproduct":
                        intent= commons.createIntent(ProductTaskActivity.this,ProductManageActivity.class);
                        intent.putExtra("STATUS", HAIRes.getInstance().PRODUCT_HELP_SCAN);
                        break;
                    case "tracking":
                        commons.startActivity(ProductTaskActivity.this, TrackingActivity.class);
                        break;
                    case "savepoint":
                        commons.startActivity(ProductTaskActivity.this, EventSendActivity.class);

                }

                if(intent != null) {
                    startActivity(intent);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        makeRequest();


    }
    private void makeRequest() {
        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");

        Call<List<String>> call = apiInterface().getProductTask(user, token);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                hidepDialog();
                if(response.body() != null) {

                    for (String item : response.body()) {
                        TaskInfo taskInfo = new TaskInfo();
                        taskInfo.setTime(-1);
                        taskInfo.setTimeRemain(-1);
                        taskInfo.setNotes("");
                        switch (item) {

                            case "importproduct":
                                taskInfo.setCode(item);
                                taskInfo.setName("NHẬP KHO");
                                taskInfo.setImage(R.mipmap.ic_product_import);
                                break;
                            case "savepoint":
                                taskInfo.setCode(item);
                                taskInfo.setName("TÍCH ĐIỂM");
                                taskInfo.setNotes("Quét tích điểm khuyến mãi");
                                taskInfo.setImage(R.mipmap.ic_product_savepoint);
                                break;
                            case "tracking":
                                taskInfo.setCode(item);
                                taskInfo.setName("TRA CỨU");
                                taskInfo.setNotes("Tra cứu hành trình sản phẩm");
                                taskInfo.setImage(R.mipmap.ic_product_search);
                                break;
                            case "staffimportproduct":
                                taskInfo.setCode(item);
                                taskInfo.setName( "NHẬP GIÙM");
                                taskInfo.setNotes("Dành cho nhân viên thị trường");
                                taskInfo.setImage(R.mipmap.ic_product_satffhelp);
                                break;
                            case "exportproduct":
                                taskInfo.setCode(item);
                                taskInfo.setName("XUẤT KHO");
                                taskInfo.setImage(R.mipmap.ic_product_export);
                                break;
                            case "transport":
                                taskInfo.setCode(item);
                                taskInfo.setName("ĐIỀU KHO");
                                taskInfo.setNotes("Nhập kho chi nhánh khác");
                                taskInfo.setImage(R.mipmap.ic_product_dieukho);
                                break;
                        }
                        taskInfos.add(taskInfo);
                    }

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                hidepDialog();
            }
        });
    }

}
