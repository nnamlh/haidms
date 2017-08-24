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
import com.congtyhai.haidateicker.DatePickerTimeline;
import com.congtyhai.haidateicker.MonthView;
import com.congtyhai.haidms.Agency.ShowAgencyActivity;
import com.congtyhai.haidms.Agency.ShowAgencyDetailActivity;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.AgencyInfo;
import com.congtyhai.model.api.CalendarCreateSend;
import com.congtyhai.model.api.CalendarDayCreate;
import com.congtyhai.model.api.ResultInfo;
import com.congtyhai.model.app.CalendarAgencyInfo;
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

    HashMap<Integer,CalendarDayCreate> calendarDayMap = new HashMap<>();

    @BindView(R.id.txtnote)
    TextView txtNote;

    @BindView(R.id.estatus)
    Spinner eStatus;

    @BindView(R.id.enotes)
    EditText eNotes;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.txtcus)
    TextView txtcus;

    List<CalendarAgencyInfo> agencyInfos = new ArrayList<>();
    CalendarAgencyAdapter mAdapter;

    int daySelect = 1;

    @BindView(R.id.btnsavenote)
    Button btnSaveNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_calendar);
        createToolbar();
        ButterKnife.bind(this);

        createTimeLine();
        // lisst status
        createListStatus();

        mAdapter = new CalendarAgencyAdapter(agencyInfos);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                CalendarAgencyInfo info = agencyInfos.get(position);
                if (info.getCheck() == 1) {
                    agencyInfos.get(position).setCheck(0);
                    configMapItem(true, info.getCode());

                } else {
                    agencyInfos.get(position).setCheck(1);
                    configMapItem(false, info.getCode());
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        btnSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarDayMap.get(daySelect).setNotes(eNotes.getText().toString());
                commons.makeToast(CreateCalendarActivity.this, "Đã lưu").show();
            }
        });

        new ReadDataTask().execute();

    }

    private void createListStatus() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, HAIRes.getInstance().GetListStatusName());
        eStatus.setAdapter(adapter);
        eStatus.setSelection(HAIRes.getInstance().findPostitionStatus(HAIRes.getInstance().CALENDAR_CSKH));

        eStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String code = HAIRes.getInstance().statusInfos.get(i).code;
               // calendarDayMap.get(daySelect).setAgencies(new ArrayList<String>());
                calendarDayMap.get(daySelect).setStatus(code);
               // calendarDayMap.get(daySelect).setNotes("");
                changeStatus(code);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void changeStatus(String code) {

        if (code.equals(HAIRes.getInstance().CALENDAR_CSKH)) {
            eNotes.setVisibility(View.GONE);
            txtNote.setVisibility(View.GONE);
            btnSaveNote.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            txtcus.setVisibility(View.VISIBLE);
        } else if (HAIRes.getInstance().CALENDAR_OTHER.equals(code)) {
            eNotes.setVisibility(View.VISIBLE);
            txtNote.setVisibility(View.VISIBLE);
            btnSaveNote.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            txtcus.setVisibility(View.GONE);
        } else {
            eNotes.setVisibility(View.GONE);
            txtNote.setVisibility(View.GONE);
            btnSaveNote.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            txtcus.setVisibility(View.GONE);
        }
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
            for (AgencyInfo info : result) {
                CalendarAgencyInfo calendarAgencyInfo = new CalendarAgencyInfo(info.getDeputy(), info.getCode(), info.getName(), 0);
                agencyInfos.add(calendarAgencyInfo);
            }

            mAdapter.notifyDataSetChanged();

            hidepDialog();
        }
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
                eStatus.setSelection(HAIRes.getInstance().findPostitionStatus(calendarDayMap.get(daySelect).getStatus()));
                changeStatus(calendarDayMap.get(daySelect).getStatus());
                eNotes.setText(calendarDayMap.get(daySelect).getNotes());
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
        }

    }


    private void refeshList() {
        CalendarDayCreate dayCreates = calendarDayMap.get(daySelect);

        for (int i = 0; i< agencyInfos.size(); i++) {
            if (dayCreates.getAgencies().contains(agencyInfos.get(i).getCode())) {
                agencyInfos.get(i).setCheck(1);
            } else {
                agencyInfos.get(i).setCheck(0);
            }
        }

        mAdapter.notifyDataSetChanged();

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_calendar, menu);
        return true;
    }

    private boolean checkPolicy() {
        for(Map.Entry<Integer, CalendarDayCreate> entry : calendarDayMap.entrySet()) {
            // String key = entry.getKey();
            CalendarDayCreate value = entry.getValue();

            if(value.getStatus().equals(HAIRes.getInstance().CALENDAR_CSKH)) {
              //  if (value.getAgencies().size() < 4)
              //      return false;
            }

        }

        return  true;
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
                } else  {
                    commons.showAlertInfo(CreateCalendarActivity.this, "Cảnh báo", "Mỗi ngày phải thăm ít nhất 4 khách hàng", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
