package com.congtyhai.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.congtyhai.haidms.R;
import com.congtyhai.model.api.order.C1OrderInfo;

import java.util.List;

/**
 * Created by HAI on 10/30/2017.
 */

public class C1OrderAdapter extends  RecyclerView.Adapter<C1OrderAdapter.MyViewHolder> {

    List<C1OrderInfo> c1OrderInfos;

    public C1OrderAdapter(List<C1OrderInfo> c1OrderInfos) {
        this.c1OrderInfos = c1OrderInfos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.c1_order_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        C1OrderInfo info = c1OrderInfos.get(position);

        holder.code.setText(info.getCode());
        holder.agency.setText(info.getC2Name());
        holder.count.setText(info.getProductCount() + "");
        holder.date.setText(info.getDateSuggest());
    }

    @Override
    public int getItemCount() {
        return c1OrderInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView code, agency, count, date;

        public MyViewHolder(View view) {
            super(view);
            code = (TextView) view.findViewById(R.id.code);
            agency = (TextView) view.findViewById(R.id.agency);
            count = (TextView) view.findViewById(R.id.productcount);
            date = (TextView) view.findViewById(R.id.expectdate);
        }
    }
}
