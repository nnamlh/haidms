package com.congtyhai.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.NotificationInfo;

import java.util.List;

/**
 * Created by HAI on 9/15/2017.
 */

public class NotificationAdapter  extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>{


    List<NotificationInfo> notificationInfos;

    Activity activity;

    public NotificationAdapter(List<NotificationInfo> notificationInfos, Activity activity) {
        this.notificationInfos = notificationInfos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NotificationInfo info = notificationInfos.get(position);

        holder.txtTile.setText(info.getTitle());
        holder.txtMessenge.setText(info.getMessenger());
        holder.txtTime.setText(info.getTime());

        if(info.getIsRead() == 1) {
            holder.txtTime.setTextColor(Color.parseColor("#E0E0E0"));
            holder.txtMessenge.setTextColor(Color.parseColor("#E0E0E0"));
            holder.txtTime.setTextColor(Color.parseColor("#E0E0E0"));
        }
    }

    @Override
    public int getItemCount() {
        return notificationInfos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtTile, txtMessenge, txtTime;

        public MyViewHolder(View view) {
            super(view);

            txtTile = (TextView) view.findViewById(R.id.title);
            txtMessenge = (TextView) view.findViewById(R.id.messenge);
            txtTime = (TextView) view.findViewById(R.id.time);

        }
    }

}
