package com.congtyhai.dms.report;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.congtyhai.dms.BaseActivity;
import com.congtyhai.dms.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KPIActivity extends BaseActivity {

    @BindView(R.id.etype)
    Spinner eType;

    String[] arrTypeId = {"BÁO CÁO THÁNG", "BÁO CÁO NGÀY"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kpi);
        createToolbar();
        ButterKnife.bind(this);
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrTypeId);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eType.setAdapter(adapterType);



    }



}
