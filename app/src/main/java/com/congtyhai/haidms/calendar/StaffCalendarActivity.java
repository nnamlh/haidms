package com.congtyhai.haidms.calendar;

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

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StaffCalendarActivity extends BaseActivity {

    @BindView(R.id.timeline)
    DatePickerTimeline timeline;

    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_calendar);
        createToolbar();
        ButterKnife.bind(this);

        createTimeLine();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
}
