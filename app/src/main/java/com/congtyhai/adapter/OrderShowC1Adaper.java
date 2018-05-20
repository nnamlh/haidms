package com.congtyhai.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.congtyhai.haidms.R;
import com.congtyhai.model.api.SubOwner;

import java.util.List;

/**
 * Created by HAI on 10/17/2017.
 */

public class OrderShowC1Adaper extends BaseAdapter {

    private List<SubOwner> subOwnerList;

    private LayoutInflater inflater;


    public OrderShowC1Adaper(Activity activity, List<SubOwner> subOwners){
      //  subOwnerList = HAIRes.getInstance().c2Select.getC1();
        inflater = activity.getLayoutInflater();
        this.subOwnerList = subOwners;
    }

    @Override
    public int getCount() {
        return subOwnerList.size();
    }

    @Override
    public Object getItem(int i) {
        return subOwnerList.get(i);
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

        SubOwner subOwner = subOwnerList.get(i);

        store.setText(subOwner.getStore());

        if (subOwner.getCode().equals("000")) {
            deputy.setText(subOwner.getName());
        } else {
            deputy.setText(subOwner.getName() + " - " + subOwner.getCode());
        }
        return view;
    }
}
