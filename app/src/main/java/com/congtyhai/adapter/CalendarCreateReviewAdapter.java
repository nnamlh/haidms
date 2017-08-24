package com.congtyhai.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.app.CalendarCreateReviewInfo;
import com.congtyhai.util.HAIRes;

import java.util.List;

/**
 * Created by HAI on 8/25/2017.
 */

public class CalendarCreateReviewAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List<CalendarCreateReviewInfo> calendarCreateReviewInfos;

    public CalendarCreateReviewAdapter(Activity activity, List<CalendarCreateReviewInfo> calendarCreateReviewInfos) {
        this.calendarCreateReviewInfos = calendarCreateReviewInfos;
        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return calendarCreateReviewInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return calendarCreateReviewInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        CalendarCreateReviewInfo info = calendarCreateReviewInfos.get(i);
        if (view == null) {
            if (info.getType() == 1) {
                view = inflater.inflate(R.layout.calendar_create_review_iteam1, null);
            } else if (info.getType() == 2) {
                view = inflater.inflate(R.layout.calendar_create_review_item2, null);
            } else {
                view = inflater.inflate(R.layout.calendar_create_review_item3, null);
            }
        }
        if (info.getType() == 1) {
            TextView txtDay = (TextView) view.findViewById(R.id.name);
            txtDay.setText(info.getDay() + "");
        } else if (info.getType() == 2) {
            TextView txtDay = (TextView) view.findViewById(R.id.name);
            txtDay.setText(info.getDay() + "");
            TextView txtStatus = (TextView) view.findViewById(R.id.status);
            if (info.getStatus().equals(HAIRes.getInstance().CALENDAR_OTHER)) {
                txtStatus.setText("Khác: " + info.getNotes());
            } else {
                txtStatus.setText(HAIRes.getInstance().findStatusName(info.getStatus()));
            }
        } else {
            TextView txtDeputy = (TextView) view.findViewById(R.id.deputy);
            TextView txtName = (TextView) view.findViewById(R.id.name);
            txtDeputy.setText(info.getDeputy() + "");
            txtName.setText(info.getName() + "");
        }


        return view;
    }
}
