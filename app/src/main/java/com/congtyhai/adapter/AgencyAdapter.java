package com.congtyhai.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        holder.deputy.setText(agency.getDeputy() + " - " + agency.getCode());
        holder.address.setText(agency.getAddress());
        holder.phone.setText(agency.getPhone());
        holder.group.setText("Thuộc cụm: " + agency.getGroup());
        holder.rank.setText("Hạng: " + agency.getRank());
    }

    @Override
    public int getItemCount() {
        return agencyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView store, deputy, address, phone, group, rank;

        public MyViewHolder(View view) {
            super(view);
            store = (TextView) view.findViewById(R.id.store);
            deputy = (TextView) view.findViewById(R.id.deputy);
            address = (TextView) view.findViewById(R.id.address);
            phone = (TextView) view.findViewById(R.id.phone);
            group = (TextView) view.findViewById(R.id.group);
            rank = (TextView) view.findViewById(R.id.rank);
        }
    }

}
