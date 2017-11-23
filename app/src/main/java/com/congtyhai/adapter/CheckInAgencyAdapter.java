package com.congtyhai.adapter;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        holder.name.setText(info.getName() + " - " + info.getCode());

        if(info.getIsShowType() == 1) {
            holder.plan.setVisibility(View.VISIBLE);
            holder.type.setVisibility(View.VISIBLE);
            holder.type.setText(info.getCheckInName());
            if(info.getInPlan() == 1){
                holder.plan.setText("Trong kế hoạch");
            } else {
                holder.plan.setText("Ngoài kế hoạch");
            }
        } else {
            holder.plan.setVisibility(View.GONE);
            holder.type.setVisibility(View.GONE);
        }

        holder.distance.setText(info.getDistance() + " m");

        if (info.getDistance() <= HAIRes.getInstance().LIMIT_DISTANCE) {
           holder.imgLocation.setImageResource(R.mipmap.ic_location_on);
        } else {
            holder.imgLocation.setImageResource(R.mipmap.ic_location_off);
        }
    }

    @Override
    public int getItemCount() {
        return checkInAgencyInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, type, plan, distance;
        public ImageView imgLocation;
        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.txtname);
            type = (TextView) view.findViewById(R.id.txttype);
            plan = (TextView) view.findViewById(R.id.txtinplan);
            distance = (TextView) view.findViewById(R.id.txtdistance);
            imgLocation =(ImageView) view.findViewById(R.id.imglocation);
        }
    }

}
