package com.congtyhai.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.congtyhai.dms.R;
import com.congtyhai.model.api.ResultEventInfo;

import java.util.List;

/**
 * Created by HAI on 10/2/2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {


    private Context mContext;
    private List<ResultEventInfo> eventInfos;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, time;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            time = (TextView) view.findViewById(R.id.time);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

        }
    }


    public EventAdapter(Context mContext, List<ResultEventInfo> eventInfos) {
        this.mContext = mContext;
        this.eventInfos = eventInfos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ResultEventInfo info = eventInfos.get(position);

        holder.title.setText(info.getEname());
        holder.time.setText(info.getEtime());

        Glide.with(mContext).load(info.getEimage()).into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return eventInfos.size();
    }

}
