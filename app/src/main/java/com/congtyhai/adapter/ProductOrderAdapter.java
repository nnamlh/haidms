package com.congtyhai.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.congtyhai.haidms.R;
import com.congtyhai.haidms.order.ShowOrderActivity;
import com.congtyhai.haidms.showinfo.ShowProductDetailActivity;
import com.congtyhai.model.api.ProductOrder;
import com.congtyhai.util.HAIRes;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by HAI on 9/25/2017.
 */

public class ProductOrderAdapter  extends RecyclerView.Adapter<ProductOrderAdapter.MyViewHolder>{

    private List<ProductOrder> productOrders;
    private ShowOrderActivity activity;


    public ProductOrderAdapter(ShowOrderActivity activity) {
        productOrders = HAIRes.getInstance().getProductOrder();
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_order_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final ProductOrder order = productOrders.get(position);

        Glide.with(activity).load(order.getImage())
                .thumbnail(0.5f)
                .into(holder.image);

        holder.name.setText(order.getName());
        holder.group.setText(order.getGroup());
        holder.detail.setText(getOrderDetailText(order.getQuantityBox(), order.getQuantity(), order.getUnit()));
        double price = order.getPrice() * order.getQuantity();

        holder.price.setText(HAIRes.getInstance().formatMoneyToText(price));

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HAIRes.getInstance().removeProductOrderAt(position);
                activity.notifyAdapter();
                activity.resetMoneyAll();
            }
        });

        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.changeQuantity(order.getQuantity(), order.getQuantityBox(), position);
            }
        });

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.changeQuantity(order.getQuantity(), order.getQuantityBox(), position);
            }
        });

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ShowProductDetailActivity.class);
                intent.putExtra(HAIRes.getInstance().KEY_INTENT_TEMP, order.getCode());
                activity.startActivity(intent);
            }
        });
    }

    private String getOrderDetailText(int box, int quantity, String unit) {
        int countCan = quantity / box;
        int countBox = quantity - countCan*box;

        if (countCan == 0) {
            return countBox + " " + unit;
        }

        if (countBox == 0) {
            return countCan + " thùng";
        }

        return countCan + " thùng " + countBox + " " + unit;

    }

    @Override
    public int getItemCount() {
        return productOrders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, group, detail, price;
        public ImageView image, imgDelete, imgEdit;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            group = (TextView) view.findViewById(R.id.group);
            detail = (TextView) view.findViewById(R.id.detail);
            image = (ImageView) view.findViewById(R.id.image);
            imgDelete = (ImageView) view.findViewById(R.id.imgdelete);
            price = (TextView) view.findViewById(R.id.price);
            imgEdit  = (ImageView) view.findViewById(R.id.imgedit);

        }
    }
}
