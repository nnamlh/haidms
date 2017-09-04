package com.congtyhai.haidms.calendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.congtyhai.adapter.AgencyAdapter;
import com.congtyhai.adapter.CalendarAgencyAdapter;
import com.congtyhai.adapter.CalendarStatusAdapter;
import com.congtyhai.adapter.CommonItemAdapter;
import com.congtyhai.haidateicker.DatePickerTimeline;
import com.congtyhai.haidateicker.MonthView;
import com.congtyhai.haidms.Agency.ShowAgencyActivity;
import com.congtyhai.haidms.Agency.ShowAgencyDetailActivity;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.AgencyInfo;
import com.congtyhai.model.api.CalendarCreateSend;
import com.congtyhai.model.api.CalendarDayCreate;
import com.congtyhai.model.api.CalendarStatus;
import com.congtyhai.model.api.ResultInfo;
import com.congtyhai.model.app.CalendarAgencyInfo;
import com.congtyhai.model.app.CommonItemInfo;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.DividerItemDecoration;
import com.congtyhai.view.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCalendarActivity extends BaseActivity {

    int month;
    int year;
    int days;

    @BindView(R.id.timeline)
    DatePickerTimeline timeline;

    @BindView(R.id.estatus)
    Spinner eStatus;

    @BindView(R.id.egroup)
    Spinner eGroups;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.txtcus)
    TextView txtcus;

    HashMap<Integer, List<CalendarAgencyInfo>> calendarAgencyMap;

    CalendarAgencyAdapter mAdapter;

    List<CommonItemInfo> groups;

    HashMap<Integer, CalendarDayCreate> calendarDayMap;

    // danh sach nhom chon hien tai
    HashMap<Integer, Integer> dayGroupAgencyChooseMap;

    List<CalendarAgencyInfo> agencyInfos;

    int daySelect = 1;
    int groupSelect = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_calendar);
        createToolbar();
        ButterKnife.bind(this);

        calendarDayMap = new HashMap<>();
        calendarAgencyMap = new HashMap<>();
        dayGroupAgencyChooseMap = new HashMap<>();
        agencyInfos = new ArrayList<>();
        groups = new ArrayList<>();

        createTimeLine();
        // lisst status
        createListStatus();


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
                boolean isCheck = true;
                if (info.getCheck() == 1) {
                    agencyInfos.get(position).setCheck(0);
                    agencyInfos.get(position).removeDayChoose(daySelect);
                    configMapItem(true, info.getCode());
                    isCheck = false;
                } else {
                    agencyInfos.get(position).addDayChoose(daySelect);
                    agencyInfos.get(position).setCheck(1);
                    configMapItem(false, info.getCode());
                    isCheck = true;
                }
                mAdapter.notifyDataSetChanged();

                for (Map.Entry<Integer, List<CalendarAgencyInfo>> entry : calendarAgencyMap.entrySet()) {
                    List<CalendarAgencyInfo> values = entry.getValue();
                    for (int i = 0; i < values.size(); i++) {
                        if (info.getCode().equals(values.get(i).getCode())) {
                            if (isCheck) {
                                entry.getValue().get(i).addDayChoose(daySelect);
                            } else {
                                entry.getValue().get(i).removeDayChoose(daySelect);
                            }
                        }
                    }
                }


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        new ReadDataTask().execute();

    }

    private void setStatusName() {
        int count = calendarDayMap.get(daySelect).getAgencies().size();
        txtcus.setText("Số khách hàng chọn: " + count);
    }

    private void createListStatus() {
        CalendarStatusAdapter adapter = new CalendarStatusAdapter(this, HAIRes.getInstance().statusInfos);
        eStatus.setAdapter(adapter);
        eStatus.setSelection(HAIRes.getInstance().findPostitionStatus(HAIRes.getInstance().CALENDAR_CSKH));

        eStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String code = HAIRes.getInstance().statusInfos.get(i).id;
                calendarDayMap.get(daySelect).setStatus(code);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void createListGroup() {
        CommonItemAdapter commonItemAdapter = new CommonItemAdapter(CreateCalendarActivity.this, groups);
        eGroups.setAdapter(commonItemAdapter);
        eGroups.setSelection(findPostionGroup());
        eGroups.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CommonItemInfo commonItemInfo = groups.get(i);
                groupSelect = Integer.parseInt(commonItemInfo.getCode());

                if (dayGroupAgencyChooseMap.containsKey(daySelect)) {
                    dayGroupAgencyChooseMap.remove(daySelect);
                }
                dayGroupAgencyChooseMap.put(daySelect, groupSelect);
                refeshList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                CalendarAgencyInfo calendarAgencyInfo = new CalendarAgencyInfo(info.getDeputy(), info.getCode(), info.getName(), 0);
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


    private int findPostionGroup() {
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).getCode().equals(groupSelect + "")) {
                return i;
            }
        }
        return 0;
    }

    private void makeRequest(CalendarCreateSend calendarCreateSend) {
        showpDialog();
        Call<ResultInfo> call = apiInterface().calendarCreate(calendarCreateSend);
        call.enqueue(new Callback<ResultInfo>() {
            @Override
            public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                hidepDialog();
                if (response.body().getId().equals("1")) {
                    commons.showAlertInfo(CreateCalendarActivity.this, "Cảnh báo", "Đã tạo lịch", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                } else {
                    commons.showAlertInfo(CreateCalendarActivity.this, "Cảnh báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                }


            }

            @Override
            public void onFailure(Call<ResultInfo> call, Throwable t) {
                hidepDialog();
                commons.makeToast(CreateCalendarActivity.this, "Lỗi đường truyền");
            }
        });
    }

    @Override
    public void onBackPressed() {
        commons.showAlertCancel(CreateCalendarActivity.this, "Cảnh báo", "Lịch sẽ không được lưu lại?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
    }


    private void createTimeLine() {

        Intent intent = getIntent();
        String data = intent.getStringExtra(HAIRes.getInstance().KEY_INTENT_CREATE_CALENDAR);
        String[] dataSplit = data.split("/");

        if (dataSplit.length != 2) {
            onBackPressed();
        }
        year = Integer.parseInt(dataSplit[1]);
        month = Integer.parseInt(dataSplit[0]);
        days = countDayInMonth(year, month);

        timeline.setFirstVisibleDate(year, getCalendarMonth(month), 01);
        timeline.setLastVisibleDate(year, getCalendarMonth(month), days);
        timeline.getMonthView().setVisibility(View.GONE);

        timeline.setDateLabelAdapter(new MonthView.DateLabelAdapter() {
            @Override
            public CharSequence getLabel(Calendar calendar, int index) {
                return Integer.toString(calendar.get(Calendar.MONTH) + 1) + "/" + (calendar.get(Calendar.YEAR) % 2000);
            }
        });


        timeline.setOnDateSelectedListener(new DatePickerTimeline.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int index) {
                daySelect = day;
                groupSelect = dayGroupAgencyChooseMap.get(daySelect);
                eStatus.setSelection(HAIRes.getInstance().findPostitionStatus(calendarDayMap.get(daySelect).getStatus()));
                eGroups.setSelection(findPostionGroup());
                refeshList();
            }
        });

        timeline.setSelectedDate(year, getCalendarMonth(month), 1);
        daySelect = 1;
        timeline.setFollowScroll(false);

        // add map
        for (int i = 1; i <= days; i++) {
            CalendarDayCreate calendarDayCreate = new CalendarDayCreate();
            calendarDayCreate.setAgencies(new ArrayList<String>());
            calendarDayCreate.setDay(i);
            calendarDayCreate.setNotes("");
            calendarDayCreate.setStatus(HAIRes.getInstance().CALENDAR_CSKH);
            calendarDayMap.put(i, calendarDayCreate);

            // set tat ca group hien thi tat ca
            dayGroupAgencyChooseMap.put(i, -1);

        }

    }


    private void refeshList() {

        agencyInfos.clear();
        if (groupSelect == -1) {
            for (Map.Entry<Integer, List<CalendarAgencyInfo>> entry : calendarAgencyMap.entrySet()) {
                List<CalendarAgencyInfo> values = entry.getValue();
                agencyInfos.addAll(values);
            }
        } else {
            List<CalendarAgencyInfo> values = calendarAgencyMap.get(groupSelect);
            agencyInfos.addAll(values);
        }

        CalendarDayCreate dayCreates = calendarDayMap.get(daySelect);

        for (int i = 0; i < agencyInfos.size(); i++) {
            if (dayCreates.getAgencies().contains(agencyInfos.get(i).getCode())) {
                agencyInfos.get(i).setCheck(1);
            } else {
                agencyInfos.get(i).setCheck(0);
            }
        }

        mAdapter.notifyDataSetChanged();

        setStatusName();

    }

    private void configMapItem(boolean isRemove, String code) {
        CalendarDayCreate dayCreates = calendarDayMap.get(daySelect);
        if (isRemove) {
            if (dayCreates.getAgencies().contains(code)) {
                dayCreates.getAgencies().remove(code);
            }
        } else {
            if (!dayCreates.getAgencies().contains(code)) {
                dayCreates.getAgencies().add(code);
            }
        }

        setStatusName();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_calendar, menu);
        return true;
    }

    private boolean checkPolicy() {

        // kiem tra moi khach hang phai duoc tham it nhat 1 lan
        for (Map.Entry<Integer, List<CalendarAgencyInfo>> entry : calendarAgencyMap.entrySet()) {
            List<CalendarAgencyInfo> values = entry.getValue();
            for (CalendarAgencyInfo info : values) {
                if (info.getDayChoose().size() == 0) {
                    commons.showAlertInfo(CreateCalendarActivity.this, "Cảnh báo", "Khách hàng : " + info.getDeputy() + " ( " + info.getName() + ") chưa được thăm lần nào trong tháng", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    return false;
                }
            }
        }

        // cac khach hang co lich cskh thi phai dc tham
        for (Map.Entry<Integer, CalendarDayCreate> entry : calendarDayMap.entrySet()) {
            //  String key = entry.getKey().toString();
            CalendarDayCreate value = entry.getValue();


            CalendarStatus calendarStatus = findStatusById(value.getStatus());

            if (calendarStatus == null) {
                commons.showAlertInfo(CreateCalendarActivity.this, "Cảnh báo", "Sai thông tin", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                return false;
            }

            if (calendarStatus.getCompel() == 1) {
                if (value.getAgencies().size() < calendarStatus.getNumber()) {
                    commons.showAlertInfo(CreateCalendarActivity.this, "Cảnh báo", calendarStatus.getName() + " phải đi thăm ít nhất " + calendarStatus.getNumber() + " /ngày", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    return false;
                }
            }


        }

        return true;
    }

    private CalendarStatus findStatusById(String id) {
        for (CalendarStatus item : HAIRes.getInstance().statusInfos) {
            if (item.getId().equals(id))
                return item;
        }

        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.next_action:
                if (checkPolicy()) {
                    commons.showAlertCancel(CreateCalendarActivity.this, "Cảnh báo", "Bạn không thể chỉnh sửa lại nếu tiếp tục ?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // save
                            String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
                            String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
                            // HAIRes.getInstance().calendarCreateSend = new CalendarCreateSend();
                            CalendarCreateSend calendarCreateSend = new CalendarCreateSend();
                            calendarCreateSend.setUser(user);
                            calendarCreateSend.setToken(token);
                            calendarCreateSend.setMonth(month);
                            calendarCreateSend.setYear(year);
                            calendarCreateSend.setNotes("");
                            calendarCreateSend.setItems(new ArrayList<CalendarDayCreate>());
                            for (Map.Entry<Integer, CalendarDayCreate> entry : calendarDayMap.entrySet()) {
                                // String key = entry.getKey();
                                CalendarDayCreate value = entry.getValue();
                                if (!HAIRes.getInstance().CALENDAR_CSKH.equals(value.getStatus())) {
                                    value.setAgencies(new ArrayList<String>());
                                }
                                if (!HAIRes.getInstance().CALENDAR_OTHER.equals(value.getStatus())) {
                                    value.setNotes("");
                                }

                                calendarCreateSend.getItems().add(value);
                            }

                            makeRequest(calendarCreateSend);

                        }
                    });
                } else {

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
