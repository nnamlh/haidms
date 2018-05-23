package com.congtyhai.dms.calendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.congtyhai.adapter.CalendarShowAgencyAdapter;
import com.congtyhai.adapter.CheckinFunctionAdapter;
import com.congtyhai.haidateicker.DatePickerTimeline;
import com.congtyhai.haidateicker.MonthView;
import com.congtyhai.dms.BaseActivity;
import com.congtyhai.dms.R;
import com.congtyhai.model.api.CalendarCheckResult;
import com.congtyhai.model.api.CalendarDayCreate;
import com.congtyhai.model.api.CalendarDayShow;
import com.congtyhai.model.api.CalendarShowAgency;
import com.congtyhai.model.api.CalendarShowResult;
import com.congtyhai.model.api.CalendarShowSend;
import com.congtyhai.model.api.CalendarShowStatusDetail;
import com.congtyhai.model.api.CheckCalendarUpdateResult;
import com.congtyhai.model.app.CheckInFunctionInfo;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.DividerItemDecoration;
import com.congtyhai.view.NonScrollListView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffCalendarActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.timeline)
    DatePickerTimeline timeline;

    BottomSheetDialog mBottomSheetDialog;
    List<CheckInFunctionInfo> checkInFunctionInfos;
    CheckinFunctionAdapter adapterBottom;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    HashMap<Integer, CalendarDayShow> calendarDayShowHashMap;


    @BindView(R.id.txttitle)
    TextView txtTitle;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;


    @BindView(R.id.txtstatus)
    TextView txtStatus;

    List<CalendarShowAgency> calendarShowAgencies;
    CalendarShowAgencyAdapter adapter;

    int hasApprove = 0;

    @BindView(R.id.imgedit)
    ImageView imgEdit;


    int daySelect;
    int monthSelect;
    int yearSelect;

    int REFESH_CALENDAR = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_calendar);
        createToolbar();
        ButterKnife.bind(this);
        createBottomSheet();
        fabClick();
        calendarDayShowHashMap  = new HashMap<>();
        calendarShowAgencies  = new ArrayList<>();
        adapter = new CalendarShowAgencyAdapter(calendarShowAgencies);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        makeRequestShowCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) + 1);

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarDayShow calendarDayShow = calendarDayShowHashMap.get(daySelect);

                if(calendarDayShow != null) {
                    CalendarDayCreate calendarDayCreate = new CalendarDayCreate();
                    calendarDayCreate.setAgencies(new ArrayList<String>());
                    calendarDayCreate.setDay(calendarDayShow.getDay());
                    calendarDayCreate.setNotes("");
                    calendarDayCreate.setStatus(calendarDayShow.getStatus());
                    calendarDayCreate.setAgencies(new ArrayList<String>());
                    for(CalendarShowAgency item : calendarDayShow.getAgences()) {
                        calendarDayCreate.getAgencies().add(item.getCode());
                    }
                    HAIRes.getInstance().setCalendarDayCreate(calendarDayCreate);
                    makeCheckUpdate();
                } else {
                    commons.makeToast(StaffCalendarActivity.this, "Không thể chỉnh sửa").show();
                }
            }
        });

        imgEdit.setVisibility(View.GONE);

    }

    private void showChangeDateDiaglog() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        View viewDialog = StaffCalendarActivity.this.getLayoutInflater().inflate(R.layout.diaglog_calendar_month_year, null);
        final EditText eMonth = (EditText) viewDialog.findViewById(R.id.emonth);
        final EditText eYear = (EditText) viewDialog.findViewById(R.id.eyear);
        eYear.setText("" + calendar.get(Calendar.YEAR));
        eMonth.setText("" + (calendar.get(Calendar.MONTH) + 1));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xem lịch");
        builder.setMessage("Nhập tháng và năm để xem");
        builder.setIcon(R.mipmap.ic_logo);
        builder.setView(viewDialog);
        builder.setNegativeButton("Thôi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setPositiveButton("Xem", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (!TextUtils.isEmpty(eMonth.getText().toString()) && !TextUtils.isEmpty(eYear.getText().toString())) {
                    monthSelect = Integer.parseInt(eMonth.getText().toString());
                    yearSelect = Integer.parseInt(eYear.getText().toString());
                    makeRequestShowCalendar(Integer.parseInt(eYear.getText().toString()), Integer.parseInt(eMonth.getText().toString()));
                }else {
                    commons.makeToast(StaffCalendarActivity.this, "Nhập tháng và năm").show();
                }

            }
        });

        builder.show();
    }



    private void createBottomSheet() {
        mBottomSheetDialog = new BottomSheetDialog(StaffCalendarActivity.this, R.style.MaterialDialogSheet);
        View sheetView = StaffCalendarActivity.this.getLayoutInflater().inflate(R.layout.checkin_bottom_menu, null);
        ImageView imgClose = (ImageView) sheetView.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
                fab.setVisibility(View.VISIBLE);
            }
        });

        NonScrollListView listView = (NonScrollListView) sheetView.findViewById(R.id.list);
        checkInFunctionInfos = new ArrayList<CheckInFunctionInfo>();

        adapterBottom = new CheckinFunctionAdapter(StaffCalendarActivity.this, checkInFunctionInfos);
        listView.setAdapter(adapterBottom);
        listView.setOnItemClickListener(this);

        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.setCancelable(false);
    }

    private void fabClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRequest();
            }
        });
    }

    private void makeRequest() {
        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");

        checkInFunctionInfos.clear();
        Call<CalendarCheckResult> call = apiInterface().calendarCheck(user);
        call.enqueue(new Callback<CalendarCheckResult>() {
            @Override
            public void onResponse(Call<CalendarCheckResult> call, Response<CalendarCheckResult> response) {

               if ( response.body().getMonth() == null || response.body().getMonth().size() == 0) {
                   commons.showAlertInfo(StaffCalendarActivity.this, "Thông báo", "Hiện tại bạn đã lên đủ kế hoạch", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {

                       }
                   });
               } else {
                   for (String item : response.body().getMonth()) {
                       checkInFunctionInfos.add(new CheckInFunctionInfo(item, R.mipmap.ic_add, "Tạo lịch " + item, ""));
                   }

                   HAIRes.getInstance().addListCalendarStatus(response.body().getStatus());

                   adapterBottom.notifyDataSetChanged();
                   mBottomSheetDialog.show();
                   fab.setVisibility(View.GONE);
               }
                hidepDialog();
            }

            @Override
            public void onFailure(Call<CalendarCheckResult> call, Throwable t) {
                hidepDialog();
            }
        });
    }

    private void makeCheckUpdate() {
        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");

        Call<CheckCalendarUpdateResult> call = apiInterface().checkCalendarUpdate(user, token, daySelect, monthSelect, yearSelect);
        call.enqueue(new Callback<CheckCalendarUpdateResult>() {
            @Override
            public void onResponse(Call<CheckCalendarUpdateResult> call, Response<CheckCalendarUpdateResult> response) {
                hidepDialog();
                if(response.body() != null) {
                    if (response.body().getId().equals("1")){
                        HAIRes.getInstance().addListCalendarStatus(response.body().getStatus());
                        Intent intent = commons.createIntent(StaffCalendarActivity.this, CalendarModifyActivity.class);
                        intent.putExtra(HAIRes.getInstance().KEY_INTENT_DAY, daySelect);
                        intent.putExtra(HAIRes.getInstance().KEY_INTENT_MONTH, monthSelect);
                        intent.putExtra(HAIRes.getInstance().KEY_INTENT_YEAR, yearSelect);
                        startActivityForResult(intent, REFESH_CALENDAR);
                    } else {
                        commons.showAlertInfo(StaffCalendarActivity.this, "Cảnh báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckCalendarUpdateResult> call, Throwable t) {
                hidepDialog();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REFESH_CALENDAR) {
            if (resultCode == RESULT_OK) {
                makeRequestShowCalendar(yearSelect, monthSelect);
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    private void makeRequestShowCalendar(int year, int month) {
        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
        CalendarShowSend info = new CalendarShowSend(month, year, user, token);

        Call<CalendarShowResult> call = apiInterface().calendarShow(info);

        call.enqueue(new Callback<CalendarShowResult>() {
            @Override
            public void onResponse(Call<CalendarShowResult> call, Response<CalendarShowResult> response) {

                if (response.body() != null) {

                    if (response.body().getId().equals("0")) {
                        commons.showAlertInfo(StaffCalendarActivity.this, "Cảnh báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                timeline.setVisibility(View.GONE);
                               // txtTitle.setText("Không có dữ liệu");
                            }
                        });
                    } else {
                        //
                        String title = "Lịch công tác: " + response.body().getMonth() + "/" + response.body().getYear();
                        hasApprove = response.body().getHasApprove();
                        if (response.body().getHasApprove() == 1) {
                            title+= " ( đã xác nhận)";
                        } else {
                            title+= " ( chưa xác nhận)";
                            imgEdit.setVisibility(View.VISIBLE);

                        }
                        txtTitle.setText(title);

                   //     eNotes.setText(response.body().getNotes());

                        // show status
                        String allStatus = "";
                        for(CalendarShowStatusDetail statusDetail: response.body().getStatusDetails()) {
                            allStatus += statusDetail.getStatusName() + " : " + statusDetail.getNumber() + " ngày\n";
                        }
                        txtStatus.setText(allStatus);

                        // set map
                        if (response.body().getItems() != null) {
                            for(CalendarDayShow calendar: response.body().getItems()) {
                                calendarDayShowHashMap.put(calendar.getDay(), calendar);

                                if(calendar.getStatus().equals("HOLIDAY")) {
                                    timeline.getTimelineView().addMapDateTextColor(calendar.getDay(), ContextCompat.getColor(StaffCalendarActivity.this, R.color.mti_bg_lbl_date_selected_color_red) );
                                } else if (calendar.getStatus().equals("CSKH")) {
                                    timeline.getTimelineView().addMapDateTextColor(calendar.getDay(), ContextCompat.getColor(StaffCalendarActivity.this, R.color.mti_lbl_date));
                                } else {
                                    timeline.getTimelineView().addMapDateTextColor(calendar.getDay(), ContextCompat.getColor(StaffCalendarActivity.this, R.color.mti_bg_lbl_date_selected_color_yellow));
                                }
                            }
                            createTimeLine(response.body().getYear(), response.body().getMonth());



                        } else {
                            timeline.setVisibility(View.GONE);
                            txtTitle.setText("Không có dữ liệu");
                        }
                    }


                } else {
                    timeline.setVisibility(View.GONE);
                    txtTitle.setText("Không có dữ liệu");
                }

                hidepDialog();
            }

            @Override
            public void onFailure(Call<CalendarShowResult> call, Throwable t) {
                hidepDialog();
                commons.makeToast(StaffCalendarActivity.this, "Lỗi đường truyền");
            }
        });

    }

    private void createTimeLine(int year, int month) {
        timeline.setVisibility(View.VISIBLE);
        Date date = new Date();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int showMonth = calendar.get(Calendar.MONTH) + 1;
        int showYear = calendar.get(Calendar.YEAR);
        int showDay = calendar.get(Calendar.DATE);

        if (showMonth != month || showYear != year) {
            showMonth = month;
            showYear = year;
            showDay = 1;
        }
        int days = countDayInMonth(showYear, showMonth);

        timeline.getMonthView().setVisibility(View.GONE);

        timeline.setFirstVisibleDate(showYear, getCalendarMonth(showMonth), 01);
        timeline.setLastVisibleDate(showYear,  getCalendarMonth(showMonth), days);

        /*
        timeline.setDateLabelAdapter(new MonthView.DateLabelAdapter() {
            @Override
            public CharSequence getLabel(Calendar calendar, int index) {
                return Integer.toString(calendar.get(Calendar.MONTH) + 1) + "/" + (calendar.get(Calendar.YEAR) % 2000);
            }
        });
*/

        timeline.setOnDateSelectedListener(new DatePickerTimeline.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int index) {
                daySelect = day;
                monthSelect = month + 1;
                yearSelect = year;
                refeshList(daySelect);
            }
        });



        timeline.setSelectedDate(showYear, getCalendarMonth(showMonth), 1);
        daySelect = 1;
        timeline.setFollowScroll(false);
        refeshList(daySelect);
    }

    private  void refeshList(int day) {
        CalendarDayShow calendarDayShow = calendarDayShowHashMap.get(day);
        calendarShowAgencies.clear();
        if (calendarDayShow != null) {
            if (calendarDayShow.getAgences() != null) {
                for (CalendarShowAgency item :calendarDayShow.getAgences() )
                    calendarShowAgencies.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CheckInFunctionInfo info = checkInFunctionInfos.get(i);
        if (info.getCode() == HAIRes.getInstance().CALENDAR_MODIFY) {

        } else {
            Intent intent = commons.createIntent(StaffCalendarActivity.this, CreateCalendarActivity.class);
            intent.putExtra(HAIRes.getInstance().KEY_INTENT_CREATE_CALENDAR, info.getCodeStr());
            startActivity(intent);
        }

        mBottomSheetDialog.dismiss();
        fab.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_calendar_show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.date_action:
                showChangeDateDiaglog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
