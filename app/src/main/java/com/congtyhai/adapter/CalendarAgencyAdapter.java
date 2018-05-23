package com.congtyhai.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.congtyhai.dms.R;
import com.congtyhai.model.app.CalendarAgencyInfo;
import com.congtyhai.view.CircularTextView;

import java.util.List;

/**
 * Created by HAI on 8/25/2017.
 */

public class CalendarAgencyAdapter  extends   RecyclerView.Adapter<CalendarAgencyAdapter.MyViewHolder> {

    List<CalendarAgencyInfo> agencyInfos;
    Activity activity;

    public CalendarAgencyAdapter(List<CalendarAgencyInfo> agencyInfos, Activity activity){
        this.agencyInfos = agencyInfos;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_agency_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CalendarAgencyInfo agency = agencyInfos.get(position);
      //  holder.deputy.setText(agency.getDeputy() + " - " + agency.getCode());

        if(agency.getType().equals("CII")) {
            holder.deputy.setText(agency.getCode() + " - Đại lý cấp 2");
        } else {
            holder.deputy.setText(agency.getCode() + " - Đại lý cấp 1");
        }

        holder.name.setText(agency.getName());
        if(agency.getCheck() == 1) {
            holder.imgCheck.setVisibility(View.VISIBLE);
        } else {
            holder.imgCheck.setVisibility(View.GONE);
        }

        holder.notes.setText("Hạng: " + agency.getRank() + "\t Cụm: " + agency.getGroup());

        holder.lDay.removeAllViews();

        for(int item : agency.getDayChoose()) {
            CircularTextView circularTextView = new CircularTextView(activity);
            circularTextView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            circularTextView.setText("" + item);
            circularTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            circularTextView.setStrokeWidth(1);
            circularTextView.setStrokeColor("#00E676");
            circularTextView.setSolidColor("#00E676");

            holder.lDay.addView(circularTextView);
        }
    }

    @Override
    public int getItemCount() {
        return agencyInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  deputy, name;
        public ImageView imgCheck;
        public LinearLayout lDay;
        public TextView notes;
        public MyViewHolder(View view) {
            super(view);
            deputy = (TextView) view.findViewById(R.id.deputy);
            name = (TextView) view.findViewById(R.id.name);
            imgCheck = (ImageView) view.findViewById(R.id.imgcheck);
            lDay = (LinearLayout) view.findViewById(R.id.lday);
            notes = (TextView) view.findViewById(R.id.notes);
        }
    }
}
