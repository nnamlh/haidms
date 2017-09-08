package com.congtyhai.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.congtyhai.haidms.R;
import com.congtyhai.model.api.ProductCodeInfo;

import java.util.List;

/**
 * Created by HAI on 9/8/2017.
 */

public class ProductShowAdapter  extends  RecyclerView.Adapter<ProductShowAdapter.MyViewHolder>{


    List<ProductCodeInfo> productCodeInfos;

    public ProductShowAdapter(List<ProductCodeInfo> productCodeInfos) {
        this.productCodeInfos = productCodeInfos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.productshow_item_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProductCodeInfo productCodeInfo = productCodeInfos.get(position);
        holder.name.setText(productCodeInfo.getName());
        holder.code.setText("Mã: " + productCodeInfo.getCode());
        holder.group.setText("Nhóm: " + productCodeInfo.getGroupName());
        holder.producer.setText("Nhà sản xuất: " + productCodeInfo.getProducer());
        holder.active.setText("Hoặt tính: " + productCodeInfo.getActivce());
    }

    @Override
    public int getItemCount() {
        return productCodeInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, code, group, producer, active;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            code = (TextView) view.findViewById(R.id.code);
            producer = (TextView) view.findViewById(R.id.producer);
            active = (TextView) view.findViewById(R.id.active);
            group = (TextView) view.findViewById(R.id.group);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }
}
