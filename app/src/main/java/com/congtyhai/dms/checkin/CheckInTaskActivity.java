package com.congtyhai.dms.checkin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.congtyhai.adapter.TaskAdapter;
import com.congtyhai.dms.Agency.ShowAgencyActivity;
import com.congtyhai.dms.BaseActivity;
import com.congtyhai.dms.R;
import com.congtyhai.dms.manageorders.StaffOrderActivity;
import com.congtyhai.dms.order.ChooseSaleActivity;
import com.congtyhai.dms.product.ProductManageActivity;
import com.congtyhai.dms.showinfo.ShowProductActivity;
import com.congtyhai.model.api.CheckInSend;
import com.congtyhai.model.api.CheckInTaskResult;
import com.congtyhai.model.api.CheckInTaskSend;
import com.congtyhai.model.api.ResultInfo;
import com.congtyhai.model.api.TaskInfoResult;
import com.congtyhai.model.app.TaskInfo;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    long distance;
    String checkInID;

    int timeRemain;
    Timer timer;

    int flexible = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_task);
        createToolbar();
        ButterKnife.bind(this);

        Intent intent = getIntent();
        HAIRes.getInstance().clearProductOrder();
        agencyCode = intent.getStringExtra(HAIRes.getInstance().KEY_INTENT_AGENCY_CODE);
        distance = intent.getLongExtra(HAIRes.getInstance().KEY_INTENT_TEMP, 0);
        checkInID = intent.getStringExtra(HAIRes.getInstance().KEY_INTENT_CHECKIN_ID);

      //  HAIRes.getInstance().CurrentAgency = agencyCode;

        //
        getSupportActionBar().setTitle("Ghé thăm khách hàng: " + agencyCode);

        taskInfos = new ArrayList<>();
        adapter = new TaskAdapter(taskInfos);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        // recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        mapCodeImage = new HashMap<>();
        mapCodeImage.put("begintask", R.mipmap.ic_begin_task);
        mapCodeImage.put("ordertask", R.mipmap.ic_order_task);
        mapCodeImage.put("decortask", R.mipmap.ic_decor_task);
        mapCodeImage.put("checkremaintask", R.mipmap.ic_remain_task);
        mapCodeImage.put("collecttask", R.mipmap.ic_collect_task);
        mapCodeImage.put("endtask", R.mipmap.ic_checkin_task);
        mapCodeImage.put("barcode", R.mipmap.ic_product_satffhelp);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                TaskInfo taskInfo = taskInfos.get(position);
                if (taskInfo.getCode().equals("endtask")) {

                   checkOut();

                } else if (taskInfo.getCode().equals("decortask")) {
                    Intent intentDecor = commons.createIntent(CheckInTaskActivity.this, DecorActivity.class);
                    intentDecor.putExtra(HAIRes.getInstance().KEY_INTENT_TEMP, agencyCode);
                    intentDecor.putExtra(HAIRes.getInstance().KEY_INTENT_CHECKIN_ID, checkInID);
                    startActivity(intentDecor);
                } else if (taskInfo.getCode().equals("ordertask")) {
                    HAIRes.getInstance().inOder = 1;
                    HAIRes.getInstance().CREATE_ORDER_TYPE = 1;
                    commons.startActivity(CheckInTaskActivity.this, ChooseSaleActivity.class);
                } else if (taskInfo.getCode().equals("barcode")) {
                    Intent intentHelp = commons.createIntent(CheckInTaskActivity.this, ProductManageActivity.class);
                    intentHelp.putExtra(HAIRes.getInstance().KEY_INTENT_TEMP, agencyCode);
                    intentHelp.putExtra("STATUS", HAIRes.getInstance().PRODUCT_HELP_SCAN);
                    startActivity(intentHelp);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        makeRequest(agencyCode);

    }

    private void checkOut() {


        View checkOutView = getLayoutInflater().inflate(R.layout.content_dialog_check_out, null);
        RadioButton radioButtonYes = (RadioButton) checkOutView.findViewById(R.id.radio_button_yes);
        RadioButton radioButtonNo = (RadioButton) checkOutView.findViewById(R.id.radio_button_no);

        if (flexible == 1) {
            radioButtonNo.setText("Chụp hình");
            radioButtonYes.setText("Ghé thăm");
        } else {
            radioButtonNo.setText("Không Beam");
            radioButtonYes.setText("Có Beam");
        }


        final RadioGroup dialogRadioGroup = (RadioGroup)checkOutView.findViewById(R.id.custom_choice);

        final EditText dialogOtherText = (EditText)checkOutView.findViewById(R.id.edit);

        AlertDialog.Builder builderDialog = new AlertDialog.Builder(CheckInTaskActivity.this);

        builderDialog.setTitle("Hoàn thành ghé thăm");
        builderDialog.setIcon(R.mipmap.ic_logo);
        builderDialog.setCancelable(false);

        builderDialog.setView(checkOutView);

        builderDialog.setNegativeButton("Thôi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builderDialog.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  makeCheckIn();
                int chooseId = dialogRadioGroup.getCheckedRadioButtonId();
                String notes = "";
                String noteCode = "";
                if (chooseId == R.id.radio_button_yes) {
                   if (flexible == 1) {
                       notes = "Ghé thăm";
                       noteCode = "checkin";
                   } else {
                       notes = "Có Beam";
                       noteCode = "hasbeam";
                   }
                } else if (chooseId == R.id.radio_button_no){
                   if(flexible == 1){
                       notes = "Chụp hình";
                       noteCode = "capture";
                   } else {
                       notes = "Không Beam";
                       noteCode = "nobeam";
                   }
                } else if (chooseId == R.id.radio_button_custom) {

                    if (TextUtils.isEmpty(dialogOtherText.getText().toString())) {
                        commons.makeToast(CheckInTaskActivity.this, "Nhập ghi chú").show();
                    } else {
                        notes = dialogOtherText.getText().toString();
                        noteCode = "other";
                    }

                }

                if (!TextUtils.isEmpty(noteCode)) {
                    makeCheckIn(noteCode, notes);
                }

            }
        });

        Dialog dialogCheckOut = builderDialog.create();

        dialogCheckOut.show();
    }

    private void makeCheckIn(String noteCode, String notes) {
        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
        CheckInSend checkInSend = new CheckInSend();
        checkInSend.setLng(getLng());
        checkInSend.setLat(getLat());
        checkInSend.setUser(user);
        checkInSend.setToken(token);
        checkInSend.setAgency(agencyCode);
        checkInSend.setDistance(distance);
        checkInSend.setNoteCode(noteCode);
        checkInSend.setNotes(notes);

        Call<ResultInfo> call = apiInterface().checkIn(checkInSend);
        call.enqueue(new Callback<ResultInfo>() {
            @Override
            public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                hidepDialog();
                if (response.body() != null) {
                    if (response.body().getId().equals("1")) {
                        commons.showAlertInfo(CheckInTaskActivity.this, "Cảnh báo", "Thành công", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent newIntent = getIntent();
                                setResult(RESULT_OK, newIntent);
                                finish();
                            }
                        });
                    } else {
                        commons.showAlertInfo(CheckInTaskActivity.this, "Cảnh báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
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
            }
        });
    }

    private void startTimer() {

        timer = new Timer();
        //Set the schedule function and rate
        timer.scheduleAtFixedRate(new TimerTask() {

                                      @Override
                                      public void run() {
                                          runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  for (int i = 0; i < taskInfos.size(); i++) {
                                                      if (taskInfos.get(i).getCode().equals("endtask")) {
                                                          taskInfos.get(i).setTimeRemain(timeRemain);
                                                          timeRemain++;
                                                          break;
                                                      }
                                                  }
                                                  adapter.notifyDataSetChanged();
                                              }
                                          });
                                      }

                                  },
                0,
                60 * 1000);


    }

    private void makeRequest(String code) {
        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
        CheckInTaskSend info = new CheckInTaskSend();
        info.setUser(user);
        info.setToken(token);
        info.setCode(code);
        info.setCheckInId(checkInID);
        Call<CheckInTaskResult> call = apiInterface().checkInTask(info);
        call.enqueue(new Callback<CheckInTaskResult>() {
            @Override
            public void onResponse(Call<CheckInTaskResult> call, Response<CheckInTaskResult> response) {

                if (response.body() != null) {

                    flexible = response.body().getFlexible();

                    for (TaskInfoResult task : response.body().getTasks()) {
                        TaskInfo taskInfo = new TaskInfo();
                        taskInfo.setCode(task.getCode());
                        taskInfo.setName(task.getName());
                        taskInfo.setTime(task.getTime());

                        if (mapCodeImage.containsKey(taskInfo.getCode())) {
                            taskInfo.setImage(mapCodeImage.get(taskInfo.getCode()));
                        } else {
                            taskInfo.setImage(R.mipmap.ic_checkin_task);
                        }

                        if (taskInfo.getCode().equals("endtask")) {
                            taskInfo.setTimeRemain(response.body().getTimeRemain());
                        } else {
                            taskInfo.setTimeRemain(-1);
                        }
                        taskInfos.add(taskInfo);
                    }
                    adapter.notifyDataSetChanged();

                    timeRemain = response.body().getTimeRemain();
                    if (timeRemain == 0) {
                        commons.makeToast(CheckInTaskActivity.this, "Bạn có thể Check In").show();
                    }
                    startTimer();
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
