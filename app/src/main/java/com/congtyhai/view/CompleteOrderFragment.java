package com.congtyhai.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.congtyhai.dms.R;
import com.congtyhai.dms.order.CompleteOrderActivity;
import com.congtyhai.model.api.ProductOrder;
import com.congtyhai.model.api.TypeCommon;
import com.congtyhai.util.HAIRes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HAI on 10/6/2017.
 */

public class CompleteOrderFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.paytype)
    RadioGroup payTypeGroup;

    @BindView(R.id.shiptype)
    RadioGroup shipTypeGroup;

    List<TypeCommon> payType;

    List<TypeCommon> shipType;

    List<Integer> payTypeRadioId;

    List<Integer> shipTypeRadioId;

    int payTypeIdStart = 1000;
    int shipTypeIdStaft = 2000;


    @BindView(R.id.store)
    TextView eStore;

    @BindView(R.id.phone)
    EditText ePhone;
    @BindView(R.id.address)
    EditText eAddress;
    @BindView(R.id.suggestdate)
    TextView eDate;
    @BindView(R.id.notes)
    EditText eNote;

    @BindView(R.id.btncontinue)
    Button btnUpdate;
    @BindView(R.id.ec1choose)
    TextView eC1Choose;

    @BindView(R.id.pricetotal)
    TextView priceTotal;

    @BindView(R.id.txtdebttime)
    TextView txtDebtTime;

    @BindView(R.id.sdebttime)
    Spinner sDebtTime;

    String name, store, phone, code, address, shipCode = "", payCode = "";

    DatePickerDialog datePickerDialog;

    CompleteOrderActivity activity;


    public void setData(CompleteOrderActivity activity, String name, String store, String code, String phone, String address, List<TypeCommon> payType, List<TypeCommon> shipType) {
        this.payType = payType;
        this.shipType = shipType;
        this.name = name;
        this.store = store;
        this.code = code;
        this.phone = phone;
        this.address = address;
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_complete_order, container, false);
        ButterKnife.bind(this, view);

        payTypeRadioId = new ArrayList<>();
        shipTypeRadioId = new ArrayList<>();

        if (payType == null)
            payType = new ArrayList<>();

        if (shipType == null)
            shipType = new ArrayList<>();

        eStore.setText(store+ " - " + code);

        ePhone.setText(phone);

        eAddress.setText(address);

        initDebt();

        if ("000".equals(HAIRes.getInstance().salePlace.getCode())) {
            eC1Choose.setText(HAIRes.getInstance().salePlace.getName());
        } else {
            eC1Choose.setText(HAIRes.getInstance().salePlace.getName() + " - " + HAIRes.getInstance().salePlace.getCode());
        }

        for (int i = 0; i < payType.size(); i++) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(payType.get(i).getName());
            int id = payTypeIdStart + i;
            radioButton.setId(id);
            payTypeGroup.addView(radioButton);
            payTypeRadioId.add(id);
        }

        payTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int idx = payTypeRadioId.indexOf(i);
                if (idx != -1) {
                    payCode = payType.get(idx).getCode();
                    if (payCode.equals("debt")){
                        txtDebtTime.setVisibility(View.VISIBLE);
                        sDebtTime.setVisibility(View.VISIBLE);
                    }  else {
                        txtDebtTime.setVisibility(View.GONE);
                        sDebtTime.setVisibility(View.GONE);
                    }
                }
            }
        });

        shipTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int idx = shipTypeRadioId.indexOf(i);
                if (idx != -1) {
                    shipCode = shipType.get(idx).getCode();
                }
            }
        });

        for (int i = 0; i < shipType.size(); i++) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(shipType.get(i).getName());
            int id = shipTypeIdStaft + i;
            radioButton.setId(id);
            shipTypeGroup.addView(radioButton);
            shipTypeRadioId.add(id);
        }

        createDateDialog();

        eDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkUpdate()) {

                    int debtTime = 0;

                    if (payCode.equals("debt")){
                        try {
                            debtTime = Integer.parseInt(sDebtTime.getSelectedItem().toString());
                        } catch (Exception e) {
                            debtTime = 0;
                        }
                    }

                    activity.makeUpdate(eAddress.getText().toString(), ePhone.getText().toString(), eNote.getText().toString(), shipCode, payCode,
                            eDate.getText().toString(), HAIRes.getInstance().salePlace.getCode(), debtTime);
                } else {
                    Toast.makeText(activity, "Điền đủ thông tin", Toast.LENGTH_LONG).show();
                }
            }
        });

        double price = 0;
        for (ProductOrder order : HAIRes.getInstance().getProductOrder()) {
            price += order.getPrice() * order.getQuantity();
        }
        priceTotal.setText(HAIRes.getInstance().formatMoneyToText(price));

        return view;

    }

    private void initDebt() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
                R.array.debt_time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sDebtTime.setAdapter(adapter);
    }

    private boolean checkUpdate() {
        if (TextUtils.isEmpty(eAddress.getText().toString()) || TextUtils.isEmpty(ePhone.getText().toString()) || TextUtils.isEmpty(eDate.getText().toString())) {
            return false;
        }

        if (TextUtils.isEmpty(shipCode) || TextUtils.isEmpty(payCode))
            return false;


        return true;
    }

    private void createDateDialog() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        datePickerDialog = new DatePickerDialog(
                getActivity(), CompleteOrderFragment.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        eDate.setText(day + "/" + (month + 1) + "/" + year);
    }
}
