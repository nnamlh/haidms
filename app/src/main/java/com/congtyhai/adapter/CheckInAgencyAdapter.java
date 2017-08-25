package com.congtyhai.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.congtyhai.haidms.R;
import com.congtyhai.model.app.CheckInAgencyInfo;
import com.congtyhai.util.HAIRes;

import java.util.List;

public class CheckInAgencyAdapter  extends RecyclerView.Adapter<CheckInAgencyAdapter.MyViewHolder> {

    List<CheckInAgencyInfo> checkInAgencyInfos;

    public CheckInAgencyAdapter(List<CheckInAgencyInfo> checkInAgencyInfos) {
        this.checkInAgencyInfos = checkInAgencyInfos;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checkin_agency_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CheckInAgencyInfo info = checkInAgencyInfos.get(position);
        holder.name.setText(info.getDeputy() + " - " + info.getCode());
        holder.code.setText(info.getName());
        holder.distance.setText(info.getDistance() + " m");

        if (info.getDistance() <= HAIRes.getInstance().LIMIT_DISTANCE) {
            holder.distance.setBackgroundResource(R.color.red);
            holder.itemView.setEnabled(true);
        } else {
            holder.distance.setBackgroundResource(R.color.gray_color);
            holder.itemView.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return checkInAgencyInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, code, distance;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.txtname);
            code = (TextView) view.findViewById(R.id.txtcode);
            distance = (TextView) view.findViewById(R.id.txtdistance);
        }
    }

}
