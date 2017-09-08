package com.congtyhai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.DecorImage;

import java.util.List;

/**
 * Created by HAI on 9/9/2017.
 */

public class DecorImageAdapter extends RecyclerView.Adapter<DecorImageAdapter.MyViewHolder> {

    List<DecorImage> decorImages;

    private Context mContext;

    public DecorImageAdapter(Context context, List<DecorImage> decorImages) {
        this.decorImages = decorImages;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_thumbnail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DecorImage image = decorImages.get(position);

        Glide.with(mContext).load(image.getUrl())
                .thumbnail(0.5f)
                .into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return decorImages.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }
}
