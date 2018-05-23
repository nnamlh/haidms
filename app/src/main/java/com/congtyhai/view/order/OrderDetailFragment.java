package com.congtyhai.view.order;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.congtyhai.dms.R;
import com.congtyhai.model.api.order.YourOrderInfo;
import com.congtyhai.util.HAIRes;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OrderDetailFragment extends Fragment {

    @BindView(R.id.estatus)
    EditText eStatus;

    @BindView(R.id.edstatus)
    EditText eDStatus;

    @BindView(R.id.estore)
    EditText eStore;
    @BindView(R.id.ephone)
    EditText ePhone;
    @BindView(R.id.eaddress)
    EditText eAddress;

    @BindView(R.id.emoney)
    EditText eMoney;

    @BindView(R.id.edatecreate)
    EditText eDateCreate;

    @BindView(R.id.edatesuggest)
    EditText eDateSuggest;

    @BindView(R.id.eshipinfo)
    EditText eShip;

    @BindView(R.id.epayinfo)
    EditText ePay;
    YourOrderInfo info;

    @BindView(R.id.esender)
    EditText eSender;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);

        ButterKnife.bind(this, view);
udpate();
        return view;
    }

    public void udpate() {
        info = HAIRes.getInstance().yourOrderInfo;

        eStatus.setText(info.getStatus().toUpperCase());

        eStore.setText(info.getC2Name() + " ( " + info.getC2Code() + " )");

        eAddress.setText(info.getAddress());

        ePhone.setText(info.getPhone());

        eMoney.setText(info.getMoney());

        eDateCreate.setText(info.getDate());

        eDateSuggest.setText(info.getDateSuggest());

        eShip.setText(info.getShipInfo());

        ePay.setText(info.getPayInfo());

        eDStatus.setText(info.getDeliveryStatus().toUpperCase());

        eSender.setText(info.getSenderName() + " - " + info.getSenderCode());
    }


}
