package com.congtyhai.haidms.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.congtyhai.haidateicker.DatePickerTimeline;
import com.congtyhai.haidateicker.MonthView;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.util.HAIRes;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateCalendarActivity extends BaseActivity {

    int month;
    int year;
    @BindView(R.id.timeline)
    DatePickerTimeline timeline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_calendar);
        createToolbar();
        ButterKnife.bind(this);

        createTimeLine();

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

        final Calendar cal = new GregorianCalendar(year, month, 1);
       // cal.set(year, month, 1);


        int day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        timeline.setFirstVisibleDate(Calendar.YEAR, getCalendarMonth(month), 01);
        timeline.setLastVisibleDate(Calendar.YEAR, getCalendarMonth(month), day);
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
        timeline.setFollowScroll(false);
    }

    private int getCalendarMonth(int month) {
        switch (month) {
            case 1:
                return Calendar.JANUARY;
            case 2:
                return  Calendar.FEBRUARY;
            case  3:
                return Calendar.MARCH;
            case  4:
                return Calendar.APRIL;
            case 5:
                return Calendar.MAY;
            case  6:
                return Calendar.JUNE;
            case 7:
                return Calendar.JULY;
            case  8:
                return  Calendar.AUGUST;
            case  9:
                return  Calendar.SEPTEMBER;
            case 10:
                return Calendar.OCTOBER;
            case 11:
                return Calendar.NOVEMBER;
            case 12:
                return Calendar.DECEMBER;
            default:
                return 1;
        }
    }


}
