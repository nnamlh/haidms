package com.congtyhai.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.congtyhai.dms.R;
import com.congtyhai.model.app.TaskInfo;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder>{

    List<TaskInfo> taskInfos;

    public TaskAdapter(List<TaskInfo> taskInfos) {
        this.taskInfos = taskInfos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TaskInfo taskInfo = taskInfos.get(position);
        holder.name.setText(taskInfo.getName());
        holder.notes.setText(taskInfo.getNotes());

        holder.image.setBackgroundResource(taskInfo.getImage());

        if (taskInfo.getCode().equals("begintask")){
            holder.notes.setText("Bạn đã ghé thăm");
        } else if (taskInfo.getCode().equals("endtask")){
            holder.notes.setText("Thời gian đã ghé thăm: " + taskInfo.getTimeRemain() + " phút");
        } else {
            holder.notes.setText(taskInfo.getNotes());
        }

        /*
        if(taskInfo.getTimeRemain() == 0) {
            holder.notes.setText("Kết thúc quy trình ghé thăm");
        } else
        if(taskInfo.getTimeRemain() != -1) {
            holder.notes.setText("Thời gian còn lại để kết thúc: " + taskInfo.getTimeRemain() + " phút");
        }*/
    }

    @Override
    public int getItemCount() {
        return taskInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, notes;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            notes = (TextView) view.findViewById(R.id.notes);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }

}
