package com.congtyhai.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.congtyhai.dms.R;
import com.congtyhai.dms.manageorders.YourOrderProductActivity;
import com.congtyhai.model.api.order.OrderProductResult;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.order.OrderProductFragment;

import java.util.List;

/**
 * Created by HAI on 11/5/2017.
 */

public class YourOrderProductAdapter extends RecyclerView.Adapter<YourOrderProductAdapter.MyViewHolder> {

    List<OrderProductResult> orderProductResults;

    YourOrderProductActivity activity;

    OrderProductFragment fragment;

    public YourOrderProductAdapter(List<OrderProductResult> orderProductResults, YourOrderProductActivity activity, OrderProductFragment fragment) {
        this.orderProductResults = orderProductResults;
        this.activity = activity;
        this.fragment = fragment;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.your_order_product_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final OrderProductResult info = orderProductResults.get(position);
        holder.name.setText(info.getProductName());

        holder.quantity.setText("SL đặt: " + getOrderDetailText(info.getQuantityBox(), info.getQuantity(), info.getUnit()));

        holder.finish.setText("SL giao: " + getOrderDetailText(info.getQuantityBox(), info.getQuantityFinish(), info.getUnit()));

        holder.money.setText("Tổng tiền: " + HAIRes.getInstance().formatMoneyToText(info.getPrice()));

        if(info.getHasBill() == 1)
            holder.hasBill.setText("có phiếu");
        else
            holder.hasBill.setText("không phiếu");

        if("warehouse".equals(info.getProductType())) {
            holder.ptype.setText("Loại hàng: hàng gửi kho");
        } else if ("new".equals(info.getProductType())) {
            holder.ptype.setText("Loại hàng: hàng xuất mới");
        } else{
            holder.ptype.setText("Loại hàng: chưa xác định");
        }

        if (info.getQuantityFinish() == 0) {
            holder.status.setText("chưa giao");
            holder.status.setTextColor(Color.parseColor("#D50000"));
        } else if (info.getQuantity() == info.getQuantityFinish()) {
            holder.status.setText("giao đủ");
            holder.status.setTextColor(Color.parseColor("#76FF03"));
        } else if (info.getQuantity() > info.getQuantityFinish()) {
            holder.status.setText("giao một phần");
            holder.status.setTextColor(Color.parseColor("#FFC107"));
        } else if (info.getQuantity() < info.getQuantityFinish()) {
            holder.status.setText("giao hơn");
            holder.status.setTextColor(Color.parseColor("#01579B"));
        }
        if(info.getQuantityFinish() == 0)
            holder.btnFinish.setVisibility(View.VISIBLE);
        else
            holder.btnFinish.setVisibility(View.GONE);

        holder.eBox.setText("");

        holder.eCan.setText("");

        holder.btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment.sendDelivery(info.getQuantity(), position);
            }
        });

        holder.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(holder.eBox.getText()))
                    holder.eBox.setText("0");

                if(TextUtils.isEmpty(holder.eCan.getText()))
                    holder.eCan.setText("0");

                int quantityCan  = Integer.parseInt(holder.eCan.getText().toString());
                int quantityBox = Integer.parseInt(holder.eBox.getText().toString());

                int quantity = quantityBox + info.getQuantityBox()*quantityCan;

                fragment.sendDelivery(quantity, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderProductResults.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, quantity, finish, money, status, hasBill, ptype;
        public EditText eCan, eBox;
        public Button btnSend, btnFinish;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.ename);
            quantity = (TextView) view.findViewById(R.id.equantity);
            finish = (TextView) view.findViewById(R.id.equantityfinish);
            money = (TextView) view.findViewById(R.id.money);
            hasBill = (TextView) view.findViewById(R.id.eHasBill);
            status = (TextView) view.findViewById(R.id.estatus);
            eCan =  (EditText) view.findViewById(R.id.ecan);
            eBox = (EditText) view.findViewById(R.id.ebox);

            btnSend = (Button) view.findViewById(R.id.btnsend);
            btnFinish = (Button) view.findViewById(R.id.btnfinish);

            ptype = (TextView) view.findViewById(R.id.etype);
        }
    }


    private String getOrderDetailText(int box, int quantity, String unit) {
        int countCan = quantity / box;
        int countBox = quantity - countCan * box;

        if (countCan == 0) {
            return countBox + " " + unit;
        }

        if (countBox == 0) {
            return countCan + " thùng";
        }

        return countCan + " thùng " + countBox + " " + unit;

    }

}
