package com.congtyhai.view;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.congtyhai.haidms.R;

/**
 * Created by HAI on 9/9/2017.
 */

public class SlideshowDialogFragment extends DialogFragment {

    public static SlideshowDialogFragment newInstance() {
        SlideshowDialogFragment f = new SlideshowDialogFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.image_fullscreen_preview, container, false);


        String url = getArguments().getString("images");

        ImageView imageView = (ImageView) v.findViewById(R.id.image_preview);

        Glide.with(getActivity()).load(url)
                .thumbnail(0.5f)
                .into(imageView);

        return v;
    }

}
