package com.congtyhai.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.congtyhai.haidms.R;
import com.congtyhai.model.api.order.C1OrderInfo;

import java.util.List;

/**
 * Created by HAI on 10/30/2017.
 */

public class C1OrderAdapter extends BaseAdapter{

    List<C1OrderInfo> c1OrderInfos;
    LayoutInflater  inflater;

    public C1OrderAdapter(List<C1OrderInfo> c1OrderInfos, Activity activity) {
        this.c1OrderInfos = c1OrderInfos;
        this.inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return c1OrderInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return c1OrderInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(view == null) {
            view = inflater.inflate(R.layout.c1_order_item, null );
        }

        TextView code, agency, count, date;
        code = (TextView) view.findViewById(R.id.code);
        agency = (TextView) view.findViewById(R.id.agency);
        count = (TextView) view.findViewById(R.id.productcount);
        date = (TextView) view.findViewById(R.id.expectdate);
        C1OrderInfo info = c1OrderInfos.get(position);

        code.setText(info.getCode());
        agency.setText(info.getC2Name() + "( " + info.getC2Code() + " )" );
        count.setText(info.getProductCount() + " sản phẩm");
        date.setText("Ngày đề nghị giao: " + info.getDateSuggest());

        return view;
    }

}
