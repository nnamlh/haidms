package com.congtyhai.view;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.congtyhai.haidms.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HAI on 10/6/2017.
 */

public class CompleteOrderFragment extends Fragment {

    @BindView(R.id.paytype)
    RadioGroup payTypeGroup;

    String[] payTypes = {"Chuyển khoản", "Công nợ", "Tiền mặt"};
    List<Integer> radioIds;

    int payTypeIdStart = 1000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_complete_order, container, false);

        radioIds = new ArrayList<>();
        ButterKnife.bind(this, view);

        for(int i = 0; i< payTypes.length; i++) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(payTypes[i]);
            int id = payTypeIdStart + i;
            radioButton.setId(id);
            payTypeGroup.addView(radioButton);
            radioIds.add(id);
        }

        payTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int idx = radioIds.indexOf(i);
                if (idx != -1 && idx < payTypes.length) {
                    Toast.makeText(getActivity(), payTypes[idx], Toast.LENGTH_LONG).show();
                }
            }
        });


        return view;

    }
}
