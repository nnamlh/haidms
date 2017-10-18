package com.congtyhai.haidms.showinfo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.haidms.Util.SimpleScanActivity;
import com.congtyhai.model.api.CheckStaffResult;
import com.congtyhai.util.HAIRes;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                intent.putExtra(HAIRes.getInstance().KEY_SCREEN_KEY_SCAN, "staff");
                startActivityForResult(intent, 1);
            }
        });
    }

    private void makeRequest(String code) {
        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
        Call<CheckStaffResult> call = apiInterface().checkStaff(user, token, code);
        call.enqueue(new Callback<CheckStaffResult>() {
            @Override
            public void onResponse(Call<CheckStaffResult> call, Response<CheckStaffResult> response) {
                hidepDialog();

                if(response.body() != null) {
                    if (response.body().getId().equals("1")) {
                        Glide.with(getApplicationContext()).load(response.body().getAvatar())
                                .thumbnail(0.5f)
                                .into(staffImage);
                        Glide.with(getApplicationContext()).load(response.body().getSignature())
                                .thumbnail(0.5f)
                                .into(staffSignture);
                        txtStt.setText(response.body().getStatus());
                    } else {
                        commons.showAlertInfo(CheckStaffActivity.this, "Thông báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    }
                }

            }

            @Override
            public void onFailure(Call<CheckStaffResult> call, Throwable t) {
                hidepDialog();
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

                        makeRequest(staffCode);

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
