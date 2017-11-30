package com.congtyhai.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.congtyhai.haidms.R;
import com.congtyhai.model.api.AgencyC2C1;
import com.congtyhai.util.HAIRes;

import java.util.List;

/**
 * Created by HAI on 10/17/2017.
 */

public class OrderShowC1Adaper extends BaseAdapter {

    private List<AgencyC2C1> agencyC2C1List;

    private LayoutInflater inflater;


    public OrderShowC1Adaper(Activity activity, List<AgencyC2C1> agencyC2C1s){
      //  agencyC2C1List = HAIRes.getInstance().c2Select.getC1();
        inflater = activity.getLayoutInflater();
        this.agencyC2C1List = agencyC2C1s;
    }

    @Override
    public int getCount() {
        return agencyC2C1List.size();
    }

    @Override
    public Object getItem(int i) {
        return agencyC2C1List.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null)
            view = inflater.inflate(R.layout.item_row , null);
        TextView store, deputy;
        store = (TextView) view.findViewById(R.id.txtcode);
        deputy = (TextView) view.findViewById(R.id.txtname);

        AgencyC2C1 agencyC2C1 = agencyC2C1List.get(i);

        store.setText(agencyC2C1.getStore());

        if (agencyC2C1.getCode().equals("000")) {
            deputy.setText(agencyC2C1.getName());
        } else {
            deputy.setText(agencyC2C1.getName() + " - " + agencyC2C1.getCode());
        }
        return view;
    }
}
