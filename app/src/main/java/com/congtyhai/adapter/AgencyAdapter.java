package com.congtyhai.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.congtyhai.haidms.R;
import com.congtyhai.model.api.AgencyInfo;

import java.util.List;

/**
 * Created by HAI on 8/23/2017.
 */

public class AgencyAdapter extends RecyclerView.Adapter<AgencyAdapter.MyViewHolder> {

    private List<AgencyInfo> agencyList;

    public AgencyAdapter(List<AgencyInfo> agencyList) {
        this.agencyList = agencyList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_agency, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AgencyInfo agency = agencyList.get(position);
        holder.store.setText(agency.getName());
        holder.type.setText( (agency.getType().equals("CII")?"Đại lý cấp 2":"Đại lý cấp 1" ) + " - " + agency.getCode());
        holder.address.setText(agency.getAddress() + " ," + agency.getDistrict() + " ," + agency.getProvince());
        holder.phone.setText(agency.getPhone());
        holder.group.setText("Thuộc cụm: " + agency.getGroup());
        holder.rank.setText(agency.getRank() == null?"":("Hạng: " + agency.getRank()));
        if (agency.getLat() == 0 || agency.getLng() == 0) {
            holder.location.setText("Chưa có tọa độ");
            holder.location.setVisibility(View.VISIBLE);
            holder.imgLocation.setVisibility(View.VISIBLE);
        } else {
            holder.location.setVisibility(View.GONE);
            holder.imgLocation.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return agencyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView store, type, address, phone, group, rank, location;
        public ImageView imgLocation;

        public MyViewHolder(View view) {
            super(view);
            store = (TextView) view.findViewById(R.id.store);
            type = (TextView) view.findViewById(R.id.type);
            address = (TextView) view.findViewById(R.id.address);
            phone = (TextView) view.findViewById(R.id.phone);
            group = (TextView) view.findViewById(R.id.group);
            rank = (TextView) view.findViewById(R.id.rank);
            location = (TextView) view.findViewById(R.id.location);
            imgLocation = (ImageView) view.findViewById(R.id.imglocation);
        }
    }

}
