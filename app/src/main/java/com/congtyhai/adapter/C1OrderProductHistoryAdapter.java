package com.congtyhai.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.congtyhai.haidms.R;
import com.congtyhai.model.api.order.OrderProductHistory;
import com.congtyhai.util.HAIRes;

import java.util.List;

/**
 * Created by HAI on 11/1/2017.
 */

public class C1OrderProductHistoryAdapter extends BaseAdapter {

    List<OrderProductHistory> orderProductHistories;

    LayoutInflater inflater;

    public C1OrderProductHistoryAdapter(Activity activity, List<OrderProductHistory> orderProductHistories) {
        this.orderProductHistories = orderProductHistories;
        inflater = activity.getLayoutInflater();
    }


    @Override
    public int getCount() {
        return orderProductHistories.size();
    }

    @Override
    public Object getItem(int i) {
        return orderProductHistories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null)
            view = inflater.inflate(R.layout.c1_order_product_history, null);

        TextView eDate = (TextView) view.findViewById(R.id.edate);

        TextView eQuantity = (TextView) view.findViewById(R.id.equantity);

        TextView eNotes = (TextView) view.findViewById(R.id.enotes);

        OrderProductHistory info = orderProductHistories.get(i);

        eDate.setText(info.getDate());
        eNotes.setText("Ghi chú: " + info.getNotes());
        eQuantity.setText("SL giao: " + HAIRes.getInstance().getOrderQuantityDetailText(info.getQuantityBox(),  info.getQuantity(), info.getUnit()));

        return view;
    }



}
