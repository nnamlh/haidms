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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ProductCodeInfo productCodeInfo = productCodeInfos.get(position);
        holder.name.setText(productCodeInfo.getName());
        holder.group.setText(productCodeInfo.getGroupName());
        holder.quantity.setText("Quy cách: " + productCodeInfo.getQuantity() + " " + productCodeInfo.getUnit() + "/ 1 thùng" );
        holder.price.setText("Giá bán lẽ: " + HAIRes.getInstance().formatMoneyToText(productCodeInfo.getPrice()));
        holder.pricebox.setText("Giá thùng: " + HAIRes.getInstance().formatMoneyToText(productCodeInfo.getPrice() * productCodeInfo.getQuantity()));

        /*
        Glide.with(activity).load(productCodeInfo.getImage())
                .thumbnail(0.5f)
                .into(holder.image);
                */


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
                    productOrder.setHasBill(1);
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
                holder.lInput.setVisibility(View.VISIBLE);
                holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HAIRes.getInstance().removeProductOrderAt(orderPosition);
                        activity.notifyAdapterProduct();
                        activity.resetCountOder();
                    }
                });

                //
                holder.eBox.setText(HAIRes.getInstance().getOrderQuantityBox(order.getQuantityBox(), order.getQuantity()) + "");
                holder.eCan.setText(HAIRes.getInstance().getOrderQuantityCan(order.getQuantityBox(), order.getQuantity()) + "");

                if (order.getHasBill() == 1) {
                    holder.chkBill.setChecked(true);
                } else{
                    holder.chkBill.setChecked(false);
                }
                holder.chkBill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b)
                            order.setHasBill(1);
                        else
                            order.setHasBill(0);
                    }
                });

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



            } else {
                holder.imgDelete.setVisibility(View.GONE);
                holder.btnOrder.setVisibility(View.VISIBLE);
                holder.lInput.setVisibility(View.GONE);
            }
        }else {
            holder.btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ShowProductDetailActivity.class);
                    intent.putExtra(HAIRes.getInstance().KEY_INTENT_TEMP, productCodeInfo.getId());
                    activity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return productCodeInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, group, quantity, price, pricebox;

        public ImageView image, imgDelete;
        public Button btnOrder, btnDetail;

        public LinearLayout lInput;

        public CheckBox chkBill;

        public EditText eCan, eBox;

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
            imgDelete = (ImageView) view.findViewById(R.id.imgdelete);
            lInput = (LinearLayout) view.findViewById(R.id.linputquantity);
            if (inOder == 1) {
                btnOrder.setVisibility(View.VISIBLE);
                btnDetail.setVisibility(View.GONE);
            } else {
                btnOrder.setVisibility(View.GONE);
                btnDetail.setVisibility(View.VISIBLE);
            }
            chkBill = (CheckBox) view.findViewById(R.id.check_has_bill);

            eBox = (EditText) view.findViewById(R.id.ebox);
            eCan = (EditText)view.findViewById(R.id.ecan);
        }
    }
}
