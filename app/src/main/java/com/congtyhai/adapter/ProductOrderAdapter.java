package com.congtyhai.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final ProductOrder order = productOrders.get(position);

        holder.name.setText(order.getName());
        holder.group.setText(order.getGroup());
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



        holder.chkBill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    order.setHasBill(1);
                } else {
                    order.setHasBill(0);
                }
            }
        });

        if (order.getHasBill() == 1)
            holder.chkBill.setChecked(true);
        else
            holder.chkBill.setChecked(false);

        holder.eBox.setText(HAIRes.getInstance().getOrderQuantityBox(order.getQuantityBox(), order.getQuantity()) + "");
        holder.eCan.setText(HAIRes.getInstance().getOrderQuantityCan(order.getQuantityBox(), order.getQuantity()) + "");
        holder.eCan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(TextUtils.isEmpty(charSequence)){

                } else {
                    order.setQuantity(HAIRes.getInstance().calQuantity(order.getQuantityBox(),
                            Integer.parseInt(holder.eCan.getText().toString()),Integer.parseInt(holder.eBox.getText().toString())));
                    activity.resetMoneyAll();
                }



            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.eBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(TextUtils.isEmpty(charSequence)){

                } else{
                    order.setQuantity(HAIRes.getInstance().calQuantity(order.getQuantityBox(),
                            Integer.parseInt(holder.eCan.getText().toString()),Integer.parseInt(holder.eBox.getText().toString())));
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }


    @Override
    public int getItemCount() {
        return productOrders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, group, price;
        public ImageView  imgDelete;
        public CheckBox chkBill;
        public EditText eCan, eBox;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            group = (TextView) view.findViewById(R.id.group);
            imgDelete = (ImageView) view.findViewById(R.id.imgdelete);
            price = (TextView) view.findViewById(R.id.price);
            chkBill = (CheckBox) view.findViewById(R.id.check_has_bill);
            eCan = (EditText) view.findViewById(R.id.ecan);
            eBox = (EditText) view.findViewById(R.id.ebox);
        }
    }
}
