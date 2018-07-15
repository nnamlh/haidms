package com.congtyhai.dms.calendar;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.congtyhai.adapter.CalendarAgencyAdapter;
import com.congtyhai.adapter.CalendarStatusAdapter;
import com.congtyhai.adapter.CommonItemAdapter;
import com.congtyhai.haidateicker.DatePickerTimeline;
import com.congtyhai.dms.BaseActivity;
import com.congtyhai.dms.R;
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

    @BindView(R.id.enotes)
    EditText eNoets;

    HashMap<String, List<CalendarAgencyInfo>> calendarAgencyMap;

    CalendarAgencyAdapter mAdapter;

    List<CommonItemInfo> groups;

    HashMap<Integer, CalendarDayCreate> calendarDayMap;

    // danh sach nhom chon hien tai
    HashMap<Integer, String> dayGroupAgencyChooseMap;

    List<CalendarAgencyInfo> agencyInfos;
    List<CalendarAgencyInfo> agencyInfosTemp;
    int daySelect = 1;
    String groupSelect = "-1";

    int maxChoose = 0;

    boolean requireCheck = true;

  //  String statusNotChoice = "NoChoice";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_calendar);
        createToolbar();
        ButterKnife.bind(this);
        Intent intent = getIntent();

        maxChoose = intent.getIntExtra("MaxChoose", 60);

        requireCheck = intent.getBooleanExtra("checkRequire", true);

        calendarDayMap = new HashMap<>();
        calendarAgencyMap = new HashMap<>();
        dayGroupAgencyChooseMap = new HashMap<>();
        agencyInfos = new ArrayList<>();
        agencyInfosTemp = new ArrayList<>();
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

                agencyInfosTemp.clear();
                agencyInfosTemp.addAll(agencyInfos);

                for (Map.Entry<String, List<CalendarAgencyInfo>> entry : calendarAgencyMap.entrySet()) {
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


        eNoets.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calendarDayMap.get(daySelect).setNotes(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        new ReadDataTask().execute();

    }

    private void setStatusName() {
        int count = calendarDayMap.get(daySelect).getAgencies().size();
        txtcus.setText("Số khách hàng chọn: " + count);
    }

    private void createListStatus() {

        CalendarStatusAdapter adapter = new CalendarStatusAdapter(this, HAIRes.getInstance().getCalendarStatuses());
        eStatus.setAdapter(adapter);

        if (!requireCheck) {
            eStatus.setSelection(HAIRes.getInstance().findPostitionStatus("HOLIDAY"));
        } else {
            eStatus.setSelection(HAIRes.getInstance().findPostitionStatus(HAIRes.getInstance().CALENDAR_CSKH));
        }


        eStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String code = HAIRes.getInstance().getCalendarStatuses().get(i).getId();
                calendarDayMap.get(daySelect).setStatus(code);

                if(code.equals("HOLIDAY")) {
                    timeline.getTimelineView().addMapDateTextColor(daySelect, ContextCompat.getColor(CreateCalendarActivity.this, R.color.mti_bg_lbl_date_selected_color_red) );
                } else if (code.equals("CSKH")) {
                    timeline.getTimelineView().addMapDateTextColor(daySelect, ContextCompat.getColor(CreateCalendarActivity.this, R.color.mti_lbl_date));
                } else if (code.equals("TVBD")) {
                    timeline.getTimelineView().addMapDateTextColor(daySelect, ContextCompat.getColor(CreateCalendarActivity.this, R.color.mti_color_blue));
                } else {
                    timeline.getTimelineView().addMapDateTextColor(daySelect, ContextCompat.getColor(CreateCalendarActivity.this, R.color.mti_bg_lbl_date_selected_color_yellow));
                }

                timeline.getTimelineView().notifiAdapter();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void createListGroup() {
        if (!requireCheck) {
            groups.clear();
            groups.add(new CommonItemInfo("Check in C2", "-1"));
            groups.add(new CommonItemInfo("Check in không cố định", "kvl"));
            calendarAgencyMap.put("kvl", new ArrayList<CalendarAgencyInfo>());
        }


        CommonItemAdapter commonItemAdapter = new CommonItemAdapter(CreateCalendarActivity.this, groups);
        eGroups.setAdapter(commonItemAdapter);
        eGroups.setSelection(findPostionGroup());
        eGroups.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CommonItemInfo commonItemInfo = groups.get(i);
                groupSelect = commonItemInfo.getCode();

                if (dayGroupAgencyChooseMap.containsKey(daySelect)) {
                    dayGroupAgencyChooseMap.remove(daySelect);
                }
                dayGroupAgencyChooseMap.put(daySelect, groupSelect);
                refeshList();
                //

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

        timeline.setOnDateSelectedListener(new DatePickerTimeline.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int index) {
                daySelect = day;
                groupSelect = dayGroupAgencyChooseMap.get(daySelect);
                String statusCode = HAIRes.getInstance().getCalendarStatuses().get(eStatus.getSelectedItemPosition()).getId();
                if (!calendarDayMap.get(daySelect).getStatus().equals(statusCode)) {
                    eStatus.setSelection(HAIRes.getInstance().findPostitionStatus(calendarDayMap.get(daySelect).getStatus()));
                }
                eGroups.setSelection(findPostionGroup());
                eNoets.setText(calendarDayMap.get(daySelect).getNotes());
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
            if(requireCheck) {
                calendarDayCreate.setStatus(HAIRes.getInstance().CALENDAR_CSKH);
                timeline.getTimelineView().addMapDateTextColor(i, ContextCompat.getColor(CreateCalendarActivity.this, R.color.mti_lbl_date) );

            }
            else
            {
                timeline.getTimelineView().addMapDateTextColor(i, ContextCompat.getColor(CreateCalendarActivity.this, R.color.mti_bg_lbl_date_selected_color_red) );
                calendarDayCreate.setStatus("HOLIDAY");
            }




            calendarDayMap.put(i, calendarDayCreate);

            // set tat ca group hien thi tat ca
            dayGroupAgencyChooseMap.put(i, "-1");

        }

    }


    private void refeshList() {

        agencyInfos.clear();

        if (groupSelect.equals("-1")) {
            for (Map.Entry<String, List<CalendarAgencyInfo>> entry : calendarAgencyMap.entrySet()) {
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


        agencyInfosTemp.clear();
        agencyInfosTemp.addAll(agencyInfos);

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
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.find_action).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                handleSearch(newText);
                return false;
            }
        });
        return true;
    }
    private void handleSearch(String query) {
        agencyInfos.clear();
        for(CalendarAgencyInfo info: agencyInfosTemp) {

            if (info.getCode().contains(query) || info.getName().contains(query))
                agencyInfos.add(info);
        }

        mAdapter.notifyDataSetChanged();
    }

    private boolean checkPolicy() {

        if (!requireCheck) {
            return true;
        }


        int countChoose = 0;
        boolean chooseEnough = false;
        // check so luong khach hang da chon
        for (Map.Entry<String, List<CalendarAgencyInfo>> entry : calendarAgencyMap.entrySet()) {
            List<CalendarAgencyInfo> values = entry.getValue();
            for (CalendarAgencyInfo info : values) {
                if (info.getDayChoose().size() != 0 && info.getType().equals("CII")) {
                    countChoose++;
                }
            }
        }

        if (calendarAgencyMap.size() > maxChoose){
            if (countChoose < maxChoose) {
                chooseEnough = false;
            }else {
                chooseEnough = true;
            }
        }

        if (!chooseEnough) {
            // kiem tra moi khach hang phai duoc tham it nhat 1 lan
            for (Map.Entry<String, List<CalendarAgencyInfo>> entry : calendarAgencyMap.entrySet()) {
                List<CalendarAgencyInfo> values = entry.getValue();
                for (CalendarAgencyInfo info : values) {
                    if (info.getDayChoose().size() == 0 && info.getType().equals("CII")) {
                        commons.showAlertInfo(CreateCalendarActivity.this, "Cảnh báo", "Khách hàng : " + info.getDeputy() + " ( " + info.getCode() + " - Cụm " + info.getGroup() + ") chưa được thăm lần nào trong tháng", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        return false;
                    }
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
                    commons.showAlertInfo(CreateCalendarActivity.this, "Cảnh báo", "Ngày " + value.getDay() + " phải đi thăm ít nhất " + calendarStatus.getNumber() + " /ngày", new DialogInterface.OnClickListener() {
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
        for (CalendarStatus item : HAIRes.getInstance().getCalendarStatuses()) {
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
                            CalendarCreateSend calendarCreateSend = new CalendarCreateSend();
                            calendarCreateSend.setUser(user);
                            calendarCreateSend.setToken(token);
                            calendarCreateSend.setMonth(month);
                            calendarCreateSend.setYear(year);
                            calendarCreateSend.setItems(new ArrayList<CalendarDayCreate>());
                            for (Map.Entry<Integer, CalendarDayCreate> entry : calendarDayMap.entrySet()) {
                                // String key = entry.getKey();
                                CalendarDayCreate value = entry.getValue();
                                calendarCreateSend.getItems().add(value);
                            }

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
