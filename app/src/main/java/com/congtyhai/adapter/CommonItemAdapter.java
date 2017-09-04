package com.congtyhai.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.congtyhai.haidms.R;
import com.congtyhai.model.app.CommonItemInfo;

import java.util.List;

/**
 * Created by HAI on 9/4/2017.
 */

public class CommonItemAdapter extends BaseAdapter {


    LayoutInflater inflater;
    List<CommonItemInfo> commonItemInfos;

    public CommonItemAdapter(Activity activity, List<CommonItemInfo> commonItemInfos) {
        this.commonItemInfos = commonItemInfos;
        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return commonItemInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return commonItemInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null)
            view = inflater.inflate(R.layout.common_item_row, null);

        TextView name = (TextView) view.findViewById(R.id.name);

        name.setText(commonItemInfos.get(i).getName());


        return view;
    }
}
