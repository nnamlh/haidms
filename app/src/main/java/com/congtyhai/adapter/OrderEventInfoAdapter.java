package com.congtyhai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.congtyhai.haidms.R;
import com.congtyhai.model.app.OrderEventInfoItem;

import java.util.List;

/**
 * Created by HAI on 10/13/2017.
 */

public class OrderEventInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<OrderEventInfoItem> items;

    public OrderEventInfoAdapter(List<OrderEventInfoItem> items) {
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new HeaderView(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_event_info_item_header, parent, false));
            case 1:
                return new ContentView(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_event_info_item_content, parent, false));
        }

        return  null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder != null) {

            OrderEventInfoItem item = items.get(position);

            switch (holder.getItemViewType()) {
                case 0:
                    HeaderView headerView = (HeaderView) holder;

                    break;
                case 1:
                    ContentView contentView = (ContentView) holder;

                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {

        OrderEventInfoItem item = items.get(position);
        return item.getType();
    }


    class HeaderView extends RecyclerView.ViewHolder {

        TextView txtEvent, txtPoint, txtTime, txtDescribe;

        public HeaderView(View view) {
            super(view);

            txtEvent = (TextView) view.findViewById(R.id.event);
            txtPoint = (TextView) view.findViewById(R.id.point);
            txtTime = (TextView) view.findViewById(R.id.time);
            txtDescribe = (TextView) view.findViewById(R.id.describe);

        }
    }

    class ContentView extends RecyclerView.ViewHolder {

        TextView txtGift;

        public ContentView(View view) {
            super(view);

            txtGift = (TextView) view.findViewById(R.id.gift);


        }
    }
}
