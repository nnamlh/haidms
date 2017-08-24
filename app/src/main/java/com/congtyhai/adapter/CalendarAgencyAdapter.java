package com.congtyhai.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.congtyhai.haidms.R;
import com.congtyhai.model.app.CalendarAgencyInfo;

import java.util.List;

/**
 * Created by HAI on 8/25/2017.
 */

public class CalendarAgencyAdapter  extends   RecyclerView.Adapter<CalendarAgencyAdapter.MyViewHolder> {

    List<CalendarAgencyInfo> agencyInfos;

    public CalendarAgencyAdapter(List<CalendarAgencyInfo> agencyInfos){
        this.agencyInfos = agencyInfos;
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
        holder.deputy.setText(agency.getDeputy() + " - " + agency.getCode());
        holder.name.setText(agency.getName());
        if(agency.getCheck() == 1) {
            holder.imgCheck.setVisibility(View.VISIBLE);
        } else {
            holder.imgCheck.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return agencyInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  deputy, name;
        public ImageView imgCheck;
        public MyViewHolder(View view) {
            super(view);
            deputy = (TextView) view.findViewById(R.id.deputy);
            name = (TextView) view.findViewById(R.id.name);
            imgCheck = (ImageView) view.findViewById(R.id.imgcheck);
        }
    }
}
