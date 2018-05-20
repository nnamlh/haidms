package com.congtyhai.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.CalendarShowAgency;
import com.congtyhai.view.CircularTextView;

import java.util.List;

/**
 * Created by HAI on 8/25/2017.
 */

public class CalendarShowAgencyAdapter  extends   RecyclerView.Adapter<CalendarShowAgencyAdapter.MyViewHolder>{


    List<CalendarShowAgency> calendarShowAgencies;

    public CalendarShowAgencyAdapter(List<CalendarShowAgency> calendarShowAgencies) {
        this.calendarShowAgencies = calendarShowAgencies;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_show_agency_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CalendarShowAgency agency = calendarShowAgencies.get(position);
       if(TextUtils.isEmpty(agency.getCode())) {
           holder.name.setText("KHÔNG THĂM KHÁCH HÀNG");
       } else {
           holder.name.setText(agency.getName());
           holder.code.setText(agency.getCode());
       }

        holder.calendar.setText(agency.getCtypename());
        if(agency.getPerform() == 1) {
            holder.imgCheck.setVisibility(View.VISIBLE);
        } else {
            holder.imgCheck.setVisibility(View.GONE);
        }

        if (agency.getInPlan() == 1) {
            holder.status.setText("Trong kế hoạch");
        } else {
            holder.status.setText("Ngoài kế hoạch");
        }


    }

    @Override
    public int getItemCount() {
        return calendarShowAgencies.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView calendar, name, status, code;
        public ImageView imgCheck;

        public MyViewHolder(View view) {
            super(view);
            calendar = (TextView) view.findViewById(R.id.calendar);
            name = (TextView) view.findViewById(R.id.name);
            status = (TextView) view.findViewById(R.id.status);
            imgCheck = (ImageView) view.findViewById(R.id.imgcheck);
            code = (TextView) view.findViewById(R.id.code);
        }
    }
}
