package com.congtyhai.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.congtyhai.haidms.R;
import com.congtyhai.model.api.BranchInfoResult;
import com.congtyhai.util.ItemRowClick;

import java.util.List;

/**
 * Created by HAI on 10/4/2017.
 */

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.MyViewHolder> {

    List<BranchInfoResult> branchInfoResults;

    ItemRowClick locationClick;

    ItemRowClick callClick;

    public BranchAdapter(List<BranchInfoResult> branchInfoResults, ItemRowClick locationClick, ItemRowClick callClick) {
        this.branchInfoResults = branchInfoResults;
        this.locationClick = locationClick;
        this.callClick = callClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.branch_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        BranchInfoResult infoResult = branchInfoResults.get(position);
        holder.name.setText(infoResult.getName());
        holder.address.setText(infoResult.getAddress());
        holder.phone.setText(infoResult.getPhone());

        holder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callClick.onClick(position);
            }
        });

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationClick.onClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return branchInfoResults.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, address, phone;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            address = (TextView) view.findViewById(R.id.address);
            image = (ImageView) view.findViewById(R.id.image);
            phone = (TextView) view.findViewById(R.id.phone);

        }
    }
}
