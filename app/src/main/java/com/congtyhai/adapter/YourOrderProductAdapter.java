package com.congtyhai.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.congtyhai.haidms.R;
import com.congtyhai.model.api.order.OrderProductResult;

import java.util.List;

/**
 * Created by HAI on 11/5/2017.
 */

public class YourOrderProductAdapter extends BaseAdapter {

    List<OrderProductResult>  orderProductResults;

    LayoutInflater inflater;

    public YourOrderProductAdapter(List<OrderProductResult> orderProductResults, Activity activity) {
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

        if(view == null)
            view = inflater.inflate(R.layout.your_order_product_item, null);

        TextView txtName = (TextView) view.findViewById(R.id.ename);

        TextView txtQuantity = (TextView) view.findViewById(R.id.equantity);

        TextView txtFinish = (TextView) view.findViewById(R.id.equantityfinish);

        TextView txtC1 = (TextView) view.findViewById(R.id.ec1);

        OrderProductResult info = orderProductResults.get(i);

        txtName.setText(info.getProductName());

        txtQuantity.setText("SL đặt: " + getOrderDetailText(info.getQuantityBox(), info.getQuantity(), info.getUnit()));

        txtFinish.setText("Đã giao: " + getOrderDetailText(info.getQuantityBox(), info.getQuantityFinish(), info.getUnit()));

        txtC1.setText("Nơi bán: " + info.getC1Store());


        return view;
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
}
