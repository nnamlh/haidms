package com.congtyhai.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.congtyhai.dms.BaseActivity;
import com.congtyhai.dms.R;
import com.congtyhai.model.app.CheckInFunctionInfo;

import java.util.List;

/**
 * Created by HAI on 8/16/2017.
 */

public class CheckinFunctionAdapter extends BaseAdapter {

    List<CheckInFunctionInfo> checkInFunctionInfos;
    LayoutInflater inflater;
    Activity activity;

    public CheckinFunctionAdapter(Activity activity, List<CheckInFunctionInfo> checkInFunctionInfos) {

        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
        this.checkInFunctionInfos = checkInFunctionInfos;
    }


    @Override
    public int getCount() {
        return checkInFunctionInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return checkInFunctionInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null) {
            view = inflater.inflate(R.layout.item_checkin_menu_bottom, null);

            ImageView image = view.findViewById(R.id.image);
            TextView txtTitle = view.findViewById(R.id.title);
            TextView txtTime = view.findViewById(R.id.time);

            CheckInFunctionInfo info = checkInFunctionInfos.get(i);

            image.setImageResource(info.getIcon());
            txtTime.setText(info.getTime());
            txtTitle.setText(info.getTitle());

        }


        return view;
    }
}
