package com.congtyhai.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.congtyhai.dms.R;
import com.congtyhai.model.api.CalendarStatus;

import java.util.List;

/**
 * Created by HAI on 9/4/2017.
 */

public class CalendarStatusAdapter extends BaseAdapter {

    Activity activity;
    LayoutInflater inflater;
    List<CalendarStatus> calendarStatuses;

    public CalendarStatusAdapter(Activity activity, List<CalendarStatus> calendarStatuses) {
        this.activity = activity;
        inflater = activity.getLayoutInflater();
        this.calendarStatuses = calendarStatuses;
    }


    @Override
    public int getCount() {
        return calendarStatuses.size();
    }

    @Override
    public Object getItem(int i) {
        return calendarStatuses.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = inflater.inflate(R.layout.calendar_status_items, null);
        }

        TextView name = (TextView) view.findViewById(R.id.name);

        TextView note = (TextView) view.findViewById(R.id.notes);

        CalendarStatus status = calendarStatuses.get(i);

        name.setText(status.getName());
        note.setText(status.getNotes());

        return view;
    }
}
