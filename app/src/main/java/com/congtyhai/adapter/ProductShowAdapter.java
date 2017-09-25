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
        holder.group.setText("Nhóm: " + productCodeInfo.getGroupName());
        holder.producer.setText("Nhà sản xuất: " + productCodeInfo.getProducer());
        holder.describe.setText(" Không độc hại với môi trường, động vật bậc cao, sản phẩm lý tưởng trong nông nghiệp xanh:Thân thiện với môi trường - Nông sản sạch - An toàn-Phù hợp với các chương trình GAP và IPM");

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
                    HAIRes.getInstance().addProductOrder(productCodeInfo);
                    activity.notifyAdapterProduct();
                    activity.resetCountOder();
                    Toast.makeText(activity, "Đã chọn mua : " + productCodeInfo.getName(), Toast.LENGTH_LONG).show();
                }
            });

            if (HAIRes.getInstance().checkExistProductOrder(productCodeInfo.getCode())) {
                holder.btnOrder.setVisibility(View.GONE);
                holder.imgCheck.setVisibility(View.VISIBLE);
            } else {
                holder.btnOrder.setVisibility(View.VISIBLE);
                holder.imgCheck.setVisibility(View.GONE);
            }
        }



    }

    @Override
    public int getItemCount() {
        return productCodeInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, group, producer, describe;
        public ImageView image, imgCheck;
        public Button btnOrder, btnDetail;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            producer = (TextView) view.findViewById(R.id.producer);
            describe = (TextView) view.findViewById(R.id.describe);
            group = (TextView) view.findViewById(R.id.group);
            image = (ImageView) view.findViewById(R.id.image);
            btnOrder = (Button) view.findViewById(R.id.btnorder);
            btnDetail = (Button) view.findViewById(R.id.btndetail);
            imgCheck = (ImageView)view.findViewById(R.id.imgcheck);

            if (inOder == 1) {
                btnOrder.setVisibility(View.VISIBLE);
            } else {
                btnOrder.setVisibility(View.GONE);
            }


        }
    }
}
