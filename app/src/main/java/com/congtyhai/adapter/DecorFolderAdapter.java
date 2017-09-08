package com.congtyhai.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.congtyhai.haidms.R;
import com.congtyhai.model.api.DecorFolder;

import java.util.List;


public class DecorFolderAdapter extends RecyclerView.Adapter<DecorFolderAdapter.MyViewHolder> {

    List<DecorFolder> decorFolders;

    public DecorFolderAdapter(List<DecorFolder> decorFolders) {
        this.decorFolders = decorFolders;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.decor_folder_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DecorFolder decorFolder = decorFolders.get(position);
        holder.name.setText(decorFolder.getName());
    }


    @Override
    public int getItemCount() {
        return decorFolders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
        }
    }
}
