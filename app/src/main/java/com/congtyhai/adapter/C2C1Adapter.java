package com.congtyhai.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.congtyhai.haidms.R;
import com.congtyhai.model.api.C2C1Info;

import java.util.List;

/**
 * Created by HAI on 10/30/2017.
 */

public class C2C1Adapter  extends RecyclerView.Adapter<C2C1Adapter.MyViewHolder> {

    private List<C2C1Info> c2C1Infos;

    public C2C1Adapter(List<C2C1Info> c2C1Infos) {
        this.c2C1Infos = c2C1Infos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_c2_c1, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        C2C1Info c2C1Info = c2C1Infos.get(position);

        holder.store.setText(c2C1Info.getName());
        holder.deputy.setText(c2C1Info.getDeputy() + " - " + c2C1Info.getCode());
        holder.address.setText(c2C1Info.getAddress());
        holder.phone.setText(c2C1Info.getPhone());

    }

    @Override
    public int getItemCount() {
        return c2C1Infos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView store, deputy, address, phone;

        public MyViewHolder(View view) {
            super(view);
            store = (TextView) view.findViewById(R.id.store);
            deputy = (TextView) view.findViewById(R.id.deputy);
            address = (TextView) view.findViewById(R.id.address);
            phone = (TextView) view.findViewById(R.id.phone);
        }
    }
}
