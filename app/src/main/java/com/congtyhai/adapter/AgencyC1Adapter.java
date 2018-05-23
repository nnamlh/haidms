package com.congtyhai.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.congtyhai.dms.R;
import com.congtyhai.model.api.AgencyC1Info;
import java.util.List;

/**
 * Created by HAI on 8/23/2017.
 */

public class AgencyC1Adapter  extends RecyclerView.Adapter<AgencyC1Adapter.MyViewHolder> {


    private List<AgencyC1Info> agencyList;

    public AgencyC1Adapter(List<AgencyC1Info> agencyList) {
        this.agencyList = agencyList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AgencyC1Info agency = agencyList.get(position);
        holder.store.setText(agency.getName());
        holder.deputy.setText(agency.getDeputy() + " - " + agency.getCode());
    }

    @Override
    public int getItemCount() {
        return agencyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView store, deputy;

        public MyViewHolder(View view) {
            super(view);
            store = (TextView) view.findViewById(R.id.txtcode);
            deputy = (TextView) view.findViewById(R.id.txtname);

        }
    }


}



