package com.congtyhai.haidms.calendar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SyncAdapterType;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.congtyhai.adapter.CalendarShowAgencyAdapter;
import com.congtyhai.adapter.CheckinFunctionAdapter;
import com.congtyhai.haidateicker.DatePickerTimeline;
import com.congtyhai.haidateicker.MonthView;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.CalendarCheckResult;
import com.congtyhai.model.api.CalendarDayShow;
import com.congtyhai.model.api.CalendarShowAgency;
import com.congtyhai.model.api.CalendarShowResult;
import com.congtyhai.model.api.CalendarShowSend;
import com.congtyhai.model.api.CalendarShowStatusDetail;
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

   // @BindView(R.id.enotes)
  //  EditText eNotes;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.txtdetail)
    TextView txtDetail;

    @BindView(R.id.txtstatus)
    TextView txtStatus;

    List<CalendarShowAgency> calendarShowAgencies;
    CalendarShowAgencyAdapter adapter;

    DatePickerDialog datePickerDialog;


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
        HAIRes.getInstance().statusInfos.clear();
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
                       checkInFunctionInfos.add(new CheckInFunctionInfo(item, R.drawable.ic_menu_send, "Tạo lịch " + item, ""));
                   }

                   HAIRes.getInstance().statusInfos.addAll(response.body().getStatus());

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
                                txtTitle.setText("Không có dữ liệu");
                            }
                        });
                    } else {
                        //
                        String title = "Lịch công tác: " + response.body().getMonth() + "/" + response.body().getYear();
                        if (response.body().getHasApprove() == 1) {
                            title+= " ( đã xác nhận)";
                        } else {
                            title+= " ( chưa xác nhận)";
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

        timeline.setDateLabelAdapter(new MonthView.DateLabelAdapter() {
            @Override
            public CharSequence getLabel(Calendar calendar, int index) {
                return Integer.toString(calendar.get(Calendar.MONTH) + 1) + "/" + (calendar.get(Calendar.YEAR) % 2000);
            }
        });


        timeline.setOnDateSelectedListener(new DatePickerTimeline.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int index) {
                CalendarDayShow calendarDayShow = calendarDayShowHashMap.get(day);
                calendarShowAgencies = new ArrayList<CalendarShowAgency>();
                if (calendarDayShow != null) {
                    txtDetail.setText(calendarDayShow.getStatusName());
                    if (calendarDayShow.getAgences() != null) {
                        for (CalendarShowAgency item :calendarDayShow.getAgences() )
                            calendarShowAgencies.add(item);
                    }
                } else {
                    txtDetail.setText("Không có dữ liệu");
                }
                adapter = new CalendarShowAgencyAdapter(calendarShowAgencies);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        timeline.setFollowScroll(false);

        timeline.setSelectedDate(showYear, getCalendarMonth(showMonth), showDay);
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
