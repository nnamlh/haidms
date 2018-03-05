package com.congtyhai.haidms.manageorders;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;

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

    String[] arrStt = {"Tất cả","Chưa giao", "Giao đủ", "Giao ít hơn", "Giao nhiều hơn"};

    String[] arrSttCode = {"","incomplete", "complete", "less", "more"};


    String[] placeName = {"Tất cả","Cấp 1", "Chi nhánh"};

    String[] placeCode = {"", "C1", "B"};

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

    public void filterClick() {
        if (TextUtils.isEmpty(fDate.getText().toString()) || TextUtils.isEmpty(tDate.getText().toString())){
            commons.makeToast(FilterActivity.this, "Chọn đủ thông tin").show();
            return;
        }

        int idx = eStatus.getSelectedItemPosition();

        if(idx != -1 && idx < arrSttCode.length) {
            Intent intent = getIntent();

            intent.putExtra("fDate", fDate.getText().toString());
            intent.putExtra("tDate", tDate.getText().toString());
            intent.putExtra("status", arrSttCode[idx]);
            intent.putExtra("agency", agency.getText().toString());
            setResult(Activity.RESULT_OK,intent);
            finish();


        }  else {
            commons.makeToast(FilterActivity.this, "Sai").show();
        }
    }
}
