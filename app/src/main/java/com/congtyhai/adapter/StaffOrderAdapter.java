package com.congtyhai.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.congtyhai.haidms.R;
import com.congtyhai.model.api.order.YourOrderInfo;

import java.util.List;

/**
 * Created by HAI on 11/1/2017.
 */

public class StaffOrderAdapter extends BaseAdapter {

    List<YourOrderInfo> staffOrderInfos;
    LayoutInflater inflater;

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

        if(view == null) {
            view = inflater.inflate(R.layout.staff_order_item, null );
        }

        TextView code, agency, count, date, datecreate, status;
        code = (TextView) view.findViewById(R.id.code);
        agency = (TextView) view.findViewById(R.id.agency);
        count = (TextView) view.findViewById(R.id.productcount);
        date = (TextView) view.findViewById(R.id.expectdate);
        datecreate = (TextView) view.findViewById(R.id.createdate);
        status = (TextView)view.findViewById(R.id.estatus);

        YourOrderInfo info = staffOrderInfos.get(position);

        code.setText("Mã hàng: " + info.getCode());
        agency.setText(info.getC2Name() + " (" + info.getC2Code() + ") ");
        count.setText(info.getProductCount() + " sản phẩm");
        date.setText("Ngày đề nghị giao: " + info.getDateSuggest());
        datecreate.setText("Ngày đặt hàng: " + info.getDate());


        status.setText(info.getStatus());

        return view;
    }

}
