package com.congtyhai.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.congtyhai.haidms.R;

/**
 * Created by HAI on 10/6/2017.
 */

public class CompleteOrderFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_complete_order, container, false);

        return view;

    }
}
