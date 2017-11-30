package com.congtyhai.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.congtyhai.haidms.R;
import com.congtyhai.haidms.showinfo.ShowProductActivity;
import com.congtyhai.haidms.showinfo.ShowProductDetailActivity;
import com.congtyhai.model.api.ProductCodeInfo;
import com.congtyhai.model.api.ProductOrder;
import com.congtyhai.util.HAIRes;

import java.util.List;

/**
 * Created by HAI on 9/8/2017.
 */

public class ProductShowAdapter extends RecyclerView.Adapter<ProductShowAdapter.MyViewHolder> {


    ShowProductActivity activity;
    List<ProductCodeInfo> productCodeInfos;
    int inOder = 0;

    public ProductShowAdapter(List<ProductCodeInfo> productCodeInfos, ShowProductActivity activity) {
        this.productCodeInfos = productCodeInfos;
        this.activity = activity;
        this.inOder = HAIRes.getInstance().inOder;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.productshow_item_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ProductCodeInfo productCodeInfo = productCodeInfos.get(position);
        holder.name.setText(productCodeInfo.getName());
        holder.group.setText(productCodeInfo.getGroupName());
        holder.quantity.setText("Quy cách: " + productCodeInfo.getQuantity() + " " + productCodeInfo.getUnit() + " /" + productCodeInfo.getQuantity_box() + " hộp/ 1 thùng" );
        holder.price.setText("Giá bán lẽ: " + HAIRes.getInstance().formatMoneyToText(productCodeInfo.getPrice()));
        holder.pricebox.setText("Giá thùng: " + HAIRes.getInstance().formatMoneyToText(productCodeInfo.getPrice() * productCodeInfo.getQuantity_box()));

        Glide.with(activity).load(productCodeInfo.getImage())
                .thumbnail(0.5f)
                .into(holder.image);

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ShowProductDetailActivity.class);
                intent.putExtra(HAIRes.getInstance().KEY_INTENT_TEMP, productCodeInfo.getId());
                activity.startActivity(intent);
            }
        });


        if(inOder == 1) {
            holder.btnOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductOrder productOrder = new ProductOrder();
                    productOrder.setCode(productCodeInfo.getId());
                    productOrder.setImage(productCodeInfo.getImage());
                    productOrder.setQuantity(productCodeInfo.getQuantity());
                    productOrder.setPrice(productCodeInfo.getPrice());
                    productOrder.setQuantityBox(productCodeInfo.getQuantity());
                    productOrder.setUnit(productCodeInfo.getUnit());
                    productOrder.setName(productCodeInfo.getName());
                    productOrder.setGroup(productCodeInfo.getGroupName());
                    HAIRes.getInstance().addProductOrder(productOrder);
                    activity.notifyAdapterProduct();
                    activity.resetCountOder();
                    Toast.makeText(activity, "Đã chọn mua : " + productCodeInfo.getName(), Toast.LENGTH_LONG).show();
                }
            });

            //
            final ProductOrder order = HAIRes.getInstance().getProductOrder(productCodeInfo.getId());

            if (order != null) {
                final int orderPosition =   HAIRes.getInstance().getProductOrderIndex(productCodeInfo.getId());
                holder.imgDelete.setVisibility(View.VISIBLE);
                holder.btnOrder.setVisibility(View.GONE);
                holder.detail.setVisibility(View.VISIBLE);
                holder.detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        activity.changeQuantity(order.getQuantity(),order.getQuantityBox(), orderPosition);
                    }
                });
                holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HAIRes.getInstance().removeProductOrderAt(orderPosition);
                        activity.notifyAdapterProduct();
                        activity.resetCountOder();
                    }
                });
                holder.detail.setText(HAIRes.getInstance().getOrderQuantityDetailText(order.getQuantityBox(), order.getQuantity(), order.getUnit()));
            } else {
                holder.imgDelete.setVisibility(View.GONE);
                holder.btnOrder.setVisibility(View.VISIBLE);
                holder.detail.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return productCodeInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, group, quantity, price, pricebox;

        public ImageView image, imgDelete;
        public Button btnOrder, btnDetail, detail;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            quantity = (TextView) view.findViewById(R.id.quantity);
            price = (TextView) view.findViewById(R.id.price);
            pricebox = (TextView) view.findViewById(R.id.pricebox);
            group = (TextView) view.findViewById(R.id.group);
            image = (ImageView) view.findViewById(R.id.image);
            btnOrder = (Button) view.findViewById(R.id.btnorder);
            btnDetail = (Button) view.findViewById(R.id.btndetail);
            detail = (Button) view.findViewById(R.id.detail);
            imgDelete = (ImageView) view.findViewById(R.id.imgdelete);
            if (inOder == 1) {
                btnOrder.setVisibility(View.VISIBLE);
            } else {
                btnOrder.setVisibility(View.GONE);
            }
        }
    }
}
