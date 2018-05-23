package com.congtyhai.dms.manageorders;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.congtyhai.dms.Agency.AddAgencyActivity;
import com.congtyhai.dms.Agency.FindAgencyC1Activity;
import com.congtyhai.dms.BaseActivity;
import com.congtyhai.dms.MainActivity;
import com.congtyhai.dms.R;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterActivity extends BaseActivity  implements DatePickerDialog.OnDateSetListener{

    @BindView(R.id.fromdate)
    TextView fDate;

    @BindView(R.id.todate)
    TextView tDate;

    @BindView(R.id.status)
    Spinner eStatus;

    @BindView(R.id.agency)
    EditText agency;

    DatePickerDialog datePickerDialog;

    int chooseDate = 0;

    @BindView(R.id.place)
    Spinner ePlace;

    @BindView(R.id.process)
    Spinner eProcess;

    String[] arrStt = {"Tất cả","Chưa giao", "Giao đủ", "Giao ít hơn", "Giao nhiều hơn"};

    String[] arrSttCode = {"","incomplete", "complete", "less", "more"};


    String[] placeName = {"Tất cả","Cấp 1", "Chi nhánh"};

    String[] placeCode = {"", "C1", "B"};

    String[] processName = {"Đang xử lý", "Hoàn thành", "Hủy"};

    String[] processId = {"process", "finish", "cancel"};

    final int C1_CODE = 2;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        createToolbar();
        ButterKnife.bind(this);

        createDateDialog();

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int showMonth = calendar.get(Calendar.MONTH) + 1;
        int showYear = calendar.get(Calendar.YEAR);

        int days = countDayInMonth(showYear, showMonth);

        fDate.setText("01/" + showMonth + "/" + showYear );

        tDate.setText(days + "/" + showMonth + "/" + showYear);

        fDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDate = 1;
                datePickerDialog.show();
            }
        });

        tDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDate = 2;
                datePickerDialog.show();
            }
        });


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrStt);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        eStatus.setAdapter(dataAdapter);


        ArrayAdapter<String> dataPlace = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, placeName);
        dataPlace.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ePlace.setAdapter(dataPlace);

        ePlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int idx, long l) {
                String placeId  = placeCode[idx];
                agency.setText("");
                if(placeId.equals("C1")){
                    agency.setVisibility(View.VISIBLE);
                } else {
                    agency.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter<String> dataProcess = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, processName);
        dataProcess.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eProcess.setAdapter(dataProcess);
    }

    public void findAgencyClick(View view) {
        Intent i = new Intent(FilterActivity.this, FindAgencyC1Activity.class);
        startActivityForResult(i, C1_CODE);
    }

    private void createDateDialog() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        datePickerDialog = new DatePickerDialog(
                this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        if (chooseDate == 1) {
            fDate.setText(day + "/" + (month + 1) + "/" + year);
        } else if(chooseDate == 2) {
            tDate.setText(day + "/" + (month + 1) + "/" + year);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == C1_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                agency.setText(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    public void filterClick(View view) {
        if (TextUtils.isEmpty(fDate.getText().toString()) || TextUtils.isEmpty(tDate.getText().toString())){
            commons.makeToast(FilterActivity.this, "Chọn đủ thông tin").show();
            return;
        }

        int idx = eStatus.getSelectedItemPosition();
        int placeIdx = ePlace.getSelectedItemPosition();
        int idxProces = eProcess.getSelectedItemPosition();

        if(idx != -1 && idx < arrSttCode.length) {
            Intent intent = getIntent();

            intent.putExtra("fDate", fDate.getText().toString());
            intent.putExtra("tDate", tDate.getText().toString());
            intent.putExtra("status", arrSttCode[idx]);
            intent.putExtra("c1Code", agency.getText().toString());
            intent.putExtra("place", placeCode[placeIdx]);
            intent.putExtra("process", processId[idx]);
            setResult(Activity.RESULT_OK,intent);
            finish();


        }  else {
            commons.makeToast(FilterActivity.this, "Sai").show();
        }
    }
}
