package com.congtyhai.haidms.calendar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import com.congtyhai.adapter.CalendarAgencyAdapter;
import com.congtyhai.adapter.CalendarStatusAdapter;
import com.congtyhai.adapter.CommonItemAdapter;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.AgencyInfo;
import com.congtyhai.model.api.CalendarDayCreate;
import com.congtyhai.model.api.CalendarStatus;
import com.congtyhai.model.api.CalendarUpdateSend;
import com.congtyhai.model.api.ResultInfo;
import com.congtyhai.model.app.CalendarAgencyInfo;
import com.congtyhai.model.app.CommonItemInfo;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.DividerItemDecoration;
import com.congtyhai.view.RecyclerTouchListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarModifyActivity extends BaseActivity {


    @BindView(R.id.estatus)
    Spinner eStatus;

    @BindView(R.id.egroup)
    Spinner eGroups;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.txtcus)
    TextView txtcus;

    @BindView(R.id.txtdate)
    TextView txtDate;

    CalendarDayCreate calendarDayCreate;

    List<CalendarAgencyInfo> agencyInfos;

    CalendarAgencyAdapter mAdapter;

    List<CommonItemInfo> groups;

    int month;
    int year;
    int days;
    int groupSelect = -1;
    boolean isUpdate = false;
    HashMap<String, List<CalendarAgencyInfo>> calendarAgencyMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_modify);
        createToolbar();
        ButterKnife.bind(this);

        calendarAgencyMap = new HashMap<>();
        calendarDayCreate = HAIRes.getInstance().getCalendarDayCreate();

        Intent intent = getIntent();
        days = intent.getIntExtra(HAIRes.getInstance().KEY_INTENT_DAY, 0);
        month = intent.getIntExtra(HAIRes.getInstance().KEY_INTENT_MONTH, 0);
        year = intent.getIntExtra(HAIRes.getInstance().KEY_INTENT_YEAR, 0);

        txtDate.setText("Ngày " + days + "/" + month + "/" + year);

        agencyInfos = new ArrayList<>();
        groups = new ArrayList<>();
        // lisst status
        createListStatus();
        setStatusName();

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new CalendarAgencyAdapter(agencyInfos, this);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // check group la group nao
                CalendarAgencyInfo info = agencyInfos.get(position);
                if (info.getCheck() == 1) {
                    agencyInfos.get(position).setCheck(0);
                   // agencyInfos.get(position).removeDayChoose(days);
                    configMapItem(true, info.getCode());
                } else {
                   // agencyInfos.get(position).addDayChoose(days);
                    agencyInfos.get(position).setCheck(1);
                    configMapItem(false, info.getCode());
                }
                mAdapter.notifyDataSetChanged();


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        eStatus.setSelection(HAIRes.getInstance().findPostitionStatus(calendarDayCreate.getStatus()));

        new ReadDataTask().execute();
    }

    private void configMapItem(boolean isRemove, String code) {

        if (isRemove) {
            if (calendarDayCreate.getAgencies().contains(code)) {
                calendarDayCreate.getAgencies().remove(code);
            }
        } else {
            if (!calendarDayCreate.getAgencies().contains(code)) {
                calendarDayCreate.getAgencies().add(code);
            }
        }

        setStatusName();
    }
    private void createListStatus() {
        CalendarStatusAdapter adapter = new CalendarStatusAdapter(this, HAIRes.getInstance().getCalendarStatuses());
        eStatus.setAdapter(adapter);
        eStatus.setSelection(HAIRes.getInstance().findPostitionStatus(HAIRes.getInstance().CALENDAR_CSKH));

        eStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String code = HAIRes.getInstance().getCalendarStatuses().get(i).id;
                calendarDayCreate.setStatus(code);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void createListGroup() {
        CommonItemAdapter commonItemAdapter = new CommonItemAdapter(CalendarModifyActivity.this, groups);
        eGroups.setAdapter(commonItemAdapter);
        eGroups.setSelection(findPostionGroup());
        eGroups.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CommonItemInfo commonItemInfo = groups.get(i);
                groupSelect = Integer.parseInt(commonItemInfo.getCode());
                refeshList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private int findPostionGroup() {
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).getCode().equals(groupSelect + "")) {
                return i;
            }
        }
        return 0;
    }

    private class ReadDataTask extends AsyncTask<String, Integer, List<AgencyInfo>> {
        protected List<AgencyInfo> doInBackground(String... urls) {

            List<AgencyInfo> data = new ArrayList<>();
            try {

                data = getListAgency();

            } catch (Exception e) {

            }
            return data;
        }

        @Override
        protected void onPreExecute() {
            showpDialog();
        }

        protected void onPostExecute(List<AgencyInfo> result) {
            groups.add(new CommonItemInfo("Tất cả ", "-1"));
            for (AgencyInfo info : result) {
                CalendarAgencyInfo calendarAgencyInfo = new CalendarAgencyInfo(info.getDeputy(), info.getCode(), info.getName(), 0, info.getType());
                calendarAgencyInfo.setDayChoose(new ArrayList<Integer>());
                calendarAgencyInfo.setGroup(info.getGroup() + "");
                calendarAgencyInfo.setRank(info.getRank());
                if (!calendarAgencyMap.containsKey(info.getGroup())) {
                    calendarAgencyMap.put(info.getGroup(), new ArrayList<CalendarAgencyInfo>());
                    groups.add(new CommonItemInfo("Cụm " + info.getGroup(), info.getGroup() + ""));
                }
                calendarAgencyMap.get(info.getGroup()).add(calendarAgencyInfo);
            }

            refeshList();
            createListGroup();
            hidepDialog();
        }
    }


    private void refeshList() {

        agencyInfos.clear();
        if (groupSelect == -1) {
            for (Map.Entry<String, List<CalendarAgencyInfo>> entry : calendarAgencyMap.entrySet()) {
                List<CalendarAgencyInfo> values = entry.getValue();
                agencyInfos.addAll(values);
            }
        } else {
            List<CalendarAgencyInfo> values = calendarAgencyMap.get(groupSelect);
            agencyInfos.addAll(values);
        }

      //  CalendarDayCreate dayCreates = calendarDayMap.get(daySelect);

        for (int i = 0; i < agencyInfos.size(); i++) {
            if (calendarDayCreate.getAgencies().contains(agencyInfos.get(i).getCode())) {
                agencyInfos.get(i).setCheck(1);
            } else {
                agencyInfos.get(i).setCheck(0);
            }
        }

        mAdapter.notifyDataSetChanged();

        setStatusName();

    }

    private void setStatusName() {
        int count = calendarDayCreate.getAgencies().size();
        txtcus.setText("Số khách hàng chọn: " + count);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_calendar, menu);
        return true;
    }
    private CalendarStatus findStatusById(String id) {
        for (CalendarStatus item : HAIRes.getInstance().getCalendarStatuses()) {
            if (item.getId().equals(id))
                return item;
        }

        return null;
    }

    private boolean checkPolicy() {


            CalendarStatus calendarStatus = findStatusById(calendarDayCreate.getStatus());

            if (calendarStatus == null) {
                commons.showAlertInfo(CalendarModifyActivity.this, "Cảnh báo", "Sai thông tin", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                return false;
            }

            if (calendarStatus.getCompel() == 1) {
                if (calendarDayCreate.getAgencies().size() < calendarStatus.getNumber()) {
                    commons.showAlertInfo(CalendarModifyActivity.this, "Cảnh báo", "Phải đi thăm ít nhất " + calendarStatus.getNumber() + " /ngày", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    return false;
                }
            }

        return true;
    }


    private void makeRequest(CalendarUpdateSend info) {
        showpDialog();
        Call<ResultInfo> call = apiInterface().updateCalendar(info);
        call.enqueue(new Callback<ResultInfo>() {
            @Override
            public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {

                hidepDialog();
                if (response.body() != null) {
                    if (response.body().getId().equals("1")) {
                        isUpdate = true;
                        commons.showAlertInfo(CalendarModifyActivity.this, "Cảnh báo", "Đã cập nhật", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    } else {
                        commons.showAlertInfo(CalendarModifyActivity.this, "Cảnh báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
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

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        if (isUpdate) {
            setResult(Activity.RESULT_OK,returnIntent);
        } else {
            setResult(Activity.RESULT_CANCELED, returnIntent);
        }
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.next_action:
                if (checkPolicy()) {
                    commons.showAlertCancel(CalendarModifyActivity.this, "Cảnh báo", "Cập nhật lịch ?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // save
                            String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
                            String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
                            CalendarUpdateSend calendarCreateSend = new CalendarUpdateSend();
                            calendarCreateSend.setUser(user);
                            calendarCreateSend.setToken(token);
                            calendarCreateSend.setMonth(month);
                            calendarCreateSend.setYear(year);
                            calendarCreateSend.setItem(calendarDayCreate);

                            makeRequest(calendarCreateSend);

                        }
                    });
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
