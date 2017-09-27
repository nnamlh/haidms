package com.congtyhai.haidms.showinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.haidms.Util.SimpleScanActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckStaffActivity extends BaseActivity {

    private String barcodeScan;

    private String staffCode;

    @BindView(R.id.staff_image)
    ImageView staffImage;

    @BindView(R.id.staff_check)
    Button btnCheck;

    @BindView(R.id.staff_name)
    TextView txtName;
    @BindView(R.id.staff_code)
    TextView txtCode;
    @BindView(R.id.staff_stt)
    TextView txtStt;
    @BindView(R.id.staff_branch)
    TextView txtBranch;
    @BindView(R.id.staff_position)
    TextView txtPosition;
    @BindView(R.id.staff_address)
    TextView txtAddress;
    @BindView(R.id.staff_signture)
    ImageView staffSignture;
    @BindView(R.id.staff_progress)
    View mProgressView;
    String TAG = CheckStaffActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_staff);
        createToolbar();
        ButterKnife.bind(this);


        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckStaffActivity.this, SimpleScanActivity.class);
                intent.putExtra("ScreenKey", "staff");
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (Activity.RESULT_OK == resultCode) {
                barcodeScan = data.getStringExtra("Content");

                try {
                    String[] arr = barcodeScan.split("\n");

                    if (arr.length == 5) {
                        staffCode = arr[0];
                        txtName.setText(arr[1]);
                        txtCode.setText(arr[0]);
                        txtPosition.setText(arr[2]);
                        txtBranch.setText(arr[3]);
                        txtAddress.setText(arr[4]);

                        //  makeJsonRequest(HaiSetting.getInstance().USER, HaiSetting.getInstance().TOKEN, staffCode);

                    } else {
                        commons.makeToast(getApplicationContext(), "Mã quét không đúng.").show();
                    }


                } catch (Exception e) {
                    commons.makeToast(getApplicationContext(), "Mã quét không đúng.").show();
                }
            } else
                commons.makeToast(this, "Cancelled").show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
