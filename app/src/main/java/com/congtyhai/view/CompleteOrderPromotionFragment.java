package com.congtyhai.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.congtyhai.adapter.OrderEventInfoAdapter;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.order.OrderEventInfo;
import com.congtyhai.model.app.OrderEventInfoItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HAI on 10/6/2017.
 */

public class CompleteOrderPromotionFragment extends Fragment {

    RecyclerView recyclerView;

    OrderEventInfoAdapter adapter;

    List<OrderEventInfoItem> items;

    public void setData(List<OrderEventInfo> orderEventInfos) {
        items = new ArrayList<>();
        for(OrderEventInfo info : orderEventInfos) {

            OrderEventInfoItem newItem = new OrderEventInfoItem(0);
            newItem.setEventId(info.getId());
            newItem.setEvent(info.getName());
            newItem.setDescribe(info.getDescribe());
            newItem.setPoint("Số điểm tích thêm: " + info.getPoint());
            newItem.setTime(info.getTime());
            newItem.setHasPoint("Tổng điểm đang tích lũy: " + info.getHasPoint());
            items.add(newItem);

            // phan thuong
            OrderEventInfoItem newGift = new OrderEventInfoItem(1);
            newGift.setAward(info.getGift().getName() + " (" + info.getGift().getPoint()+ " điểm)");
            newGift.setAwardImg(info.getGift().getImage());
            items.add(newGift);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_promotion, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        /*
        items = new ArrayList<>();

        items.add(new OrderEventInfoItem(0));

        for(int i = 0; i < 5; i++) {
            items.add(new OrderEventInfoItem(1));
        }
        items.add(new OrderEventInfoItem(0));

        for(int i = 0; i < 5; i++) {
            items.add(new OrderEventInfoItem(1));
        }
        */

        adapter = new OrderEventInfoAdapter(items, getActivity());

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);

        return view;

    }
}