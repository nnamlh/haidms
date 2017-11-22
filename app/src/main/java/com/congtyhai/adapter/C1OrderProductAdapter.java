package com.congtyhai.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.congtyhai.haidms.R;
import com.congtyhai.model.api.order.OrderProductResult;
import com.congtyhai.util.HAIRes;

import java.util.List;

/**
 * Created by HAI on 11/1/2017.
 */

public class C1OrderProductAdapter extends BaseAdapter{

    List<OrderProductResult>  orderProductResults;

    LayoutInflater inflater;

    public  C1OrderProductAdapter(List<OrderProductResult>  orderProductResults, Activity activity) {
        this.orderProductResults = orderProductResults;
        inflater = activity.getLayoutInflater();
    }


    @Override
    public int getCount() {
        return orderProductResults.size();
    }

    @Override
    public Object getItem(int i) {
        return orderProductResults.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null) {
            view = inflater.inflate(R.layout.c1_order_product_item, null);
        }

        TextView txtName = (TextView) view.findViewById(R.id.ename);

        TextView txtQuantity = (TextView) view.findViewById(R.id.equantity);

        TextView txtFinish = (TextView) view.findViewById(R.id.equantityfinish);

        OrderProductResult info = orderProductResults.get(i);

        txtName.setText(info.getProductName());

        txtQuantity.setText("SL đặt: " +  HAIRes.getInstance().getOrderQuantityDetailText(info.getQuantityBox(), info.getQuantity(), info.getUnit()));

        txtFinish.setText("Đã giao: " +  HAIRes.getInstance().getOrderQuantityDetailText(info.getQuantityBox(), info.getQuantityFinish(), info.getUnit()));

        return view;
    }



}
