package com.congtyhai.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.congtyhai.dms.R;
import com.congtyhai.model.api.order.YourOrderInfo;

import java.util.List;

/**
 * Created by HAI on 11/5/2017.
 */

public class C2OrderAdapter extends BaseAdapter {

    List<YourOrderInfo> staffOrderInfos;
    LayoutInflater inflater;

    public C2OrderAdapter(List<YourOrderInfo> staffOrderInfos, Activity activity) {
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

        TextView code, count, date, datecreate, status;
        code = (TextView) view.findViewById(R.id.code);
        count = (TextView) view.findViewById(R.id.productcount);
        date = (TextView) view.findViewById(R.id.expectdate);
        datecreate = (TextView) view.findViewById(R.id.createdate);
        status = (TextView)view.findViewById(R.id.estatus);

        YourOrderInfo info = staffOrderInfos.get(position);

        code.setText("Mã hàng: " + info.getCode());
        count.setText(info.getProductCount() + " sản phẩm");
        date.setText("Ngày đề nghị giao: " + info.getDateSuggest());
        datecreate.setText("Ngày đặt hàng: " + info.getDate());


        status.setText(info.getStatus());

        return view;
    }
}
