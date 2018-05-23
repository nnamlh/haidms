package com.congtyhai.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.congtyhai.dms.R;
import com.congtyhai.model.api.order.YourOrderInfo;

import java.util.List;

/**
 * Created by HAI on 11/1/2017.
 */

public class StaffOrderAdapter extends BaseAdapter {

    List<YourOrderInfo> staffOrderInfos;
    LayoutInflater inflater;

    Activity activity;

    public StaffOrderAdapter(List<YourOrderInfo> staffOrderInfos, Activity activity) {
        this.staffOrderInfos = staffOrderInfos;
        this.inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return staffOrderInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return staffOrderInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = inflater.inflate(R.layout.staff_order_item, null);
        }

        TextView code, agency, count, date, datecreate, status, sender, money, deliveryStatus,inCheckin;
        code = (TextView) view.findViewById(R.id.code);
        agency = (TextView) view.findViewById(R.id.agency);
        count = (TextView) view.findViewById(R.id.productcount);
        date = (TextView) view.findViewById(R.id.expectdate);
        datecreate = (TextView) view.findViewById(R.id.createdate);
        status = (TextView) view.findViewById(R.id.estatus);
        sender = (TextView) view.findViewById(R.id.sender);
        money = (TextView) view.findViewById(R.id.money);

        inCheckin = (TextView) view.findViewById(R.id.incheckin);

        deliveryStatus = (TextView) view.findViewById(R.id.deliverystatus);

        YourOrderInfo info = staffOrderInfos.get(position);

        code.setText("Mã hàng: " + info.getCode());
        agency.setText(info.getC2Name() + " (" + info.getC2Code() + ") ");
        count.setText(info.getProductCount() + " sản phẩm");
        date.setText("Đề nghị giao: " + info.getDateSuggest());
        datecreate.setText("Ngày đặt: " + info.getDate());
        sender.setText("Nơi bán: " + info.getSenderName() + " - " + info.getSenderCode());
        money.setText("Tổng tiền: " + info.getMoney());

        deliveryStatus.setText(info.getDeliveryStatus());
        if ("incomplete".equals(info.getDeliveryStatusCode())) {
            deliveryStatus.setTextColor(Color.parseColor("#D50000"));
        } else if ("complete".equals(info.getDeliveryStatusCode())) {
            deliveryStatus.setTextColor(Color.parseColor("#76FF03"));

        } else if ("less".equals(info.getDeliveryStatusCode())) {
            deliveryStatus.setTextColor(Color.parseColor("#FFC107"));

        } else if ("more".equals(info.getDeliveryStatusCode())) {
            deliveryStatus.setTextColor(Color.parseColor("#01579B"));
        }

        if(info.getInCheckin() == 1) {
            inCheckin.setText("Tạo trong checkin");
        } else {
            inCheckin.setText("Tạo ngoài checkin");
        }

        status.setText(info.getStatus());

        return view;
    }

}
