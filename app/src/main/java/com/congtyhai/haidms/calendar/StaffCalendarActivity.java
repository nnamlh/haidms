package com.congtyhai.haidms.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.congtyhai.adapter.CheckinFunctionAdapter;
import com.congtyhai.haidateicker.DatePickerTimeline;
import com.congtyhai.haidateicker.MonthView;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.MainActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.CheckCalendarResult;
import com.congtyhai.model.app.CheckInFunctionInfo;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.NonScrollListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffCalendarActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.timeline)
    DatePickerTimeline timeline;

    Calendar calendar;
    BottomSheetDialog mBottomSheetDialog;
    List<CheckInFunctionInfo> checkInFunctionInfos;
    CheckinFunctionAdapter adapterBottom;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_calendar);
        createToolbar();
        ButterKnife.bind(this);

        createTimeLine();
        createBottomSheet();
        fabClick();

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
        Call<CheckCalendarResult> call = apiInterface().checkCalendarCreate(user);
        call.enqueue(new Callback<CheckCalendarResult>() {
            @Override
            public void onResponse(Call<CheckCalendarResult> call, Response<CheckCalendarResult> response) {
                for (String item : response.body().getMonth()) {
                    checkInFunctionInfos.add(new CheckInFunctionInfo(item, R.drawable.ic_menu_send, "Tạo lịch " + item, ""));
                }

                HAIRes.getInstance().statusInfos.addAll(response.body().getStatus());

                adapterBottom.notifyDataSetChanged();
                mBottomSheetDialog.show();
                fab.setVisibility(View.GONE);
                hidepDialog();
            }

            @Override
            public void onFailure(Call<CheckCalendarResult> call, Throwable t) {
                hidepDialog();
            }
        });
    }

    private void createTimeLine() {

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

            }
        });

        timeline.setFirstVisibleDate(2017, Calendar.AUGUST, 01);
        timeline.setLastVisibleDate(2017, Calendar.AUGUST, 31);
        timeline.setFollowScroll(false);
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
}
