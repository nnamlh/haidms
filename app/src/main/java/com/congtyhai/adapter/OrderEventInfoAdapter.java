package com.congtyhai.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.congtyhai.dms.R;
import com.congtyhai.model.app.OrderEventInfoItem;

import java.util.List;

/**
 * Created by HAI on 10/13/2017.
 */

public class OrderEventInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<OrderEventInfoItem> items;

    Activity activity;
    public OrderEventInfoAdapter(List<OrderEventInfoItem> items, Activity activity) {
        this.items = items;
        this.activity = activity;
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
                    headerView.txtEvent.setText(item.getEvent());
                    headerView.txtHasPoint.setText(item.getHasPoint());
                    headerView.txtDescribe.setText(item.getDescribe());
                    headerView.txtPoint.setText(item.getPoint());
                    headerView.txtTime.setText(item.getTime());
                    break;
                case 1:
                    ContentView contentView = (ContentView) holder;
                    Glide.with(activity).load(item.getAwardImg())
                            .thumbnail(0.5f)
                            .into(contentView.imgGift);
                    contentView.txtGift.setText(item.getAward());
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

        TextView txtEvent, txtPoint, txtTime, txtDescribe, txtHasPoint;

        public HeaderView(View view) {
            super(view);

            txtEvent = (TextView) view.findViewById(R.id.event);
            txtPoint = (TextView) view.findViewById(R.id.point);
            txtTime = (TextView) view.findViewById(R.id.time);
            txtDescribe = (TextView) view.findViewById(R.id.describe);
            txtHasPoint = (TextView) view.findViewById(R.id.haspoint);

        }
    }

    class ContentView extends RecyclerView.ViewHolder {

        TextView txtGift;

        ImageView imgGift;

        public ContentView(View view) {
            super(view);

            txtGift = (TextView) view.findViewById(R.id.gift);
            imgGift = (ImageView) view.findViewById(R.id.imggift);

        }
    }
}
