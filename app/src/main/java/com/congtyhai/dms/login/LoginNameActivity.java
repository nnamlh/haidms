package com.congtyhai.dms.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.congtyhai.dms.R;
import com.congtyhai.model.api.CheckUserLoginResult;
import com.congtyhai.util.HAIRes;
import com.congtyhai.util.LoginService;
import com.congtyhai.util.ServiceGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginNameActivity extends LoginActivity {


    @BindView(R.id.ename)
    EditText eName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_name);
        ButterKnife.bind(this);
    }


    public void loginClick(View view) {
        String name = eName.getText().toString();
        String phone = commons.getPhone(LoginNameActivity.this);
        if (TextUtils.isEmpty(name)) {
            commons.makeToast(LoginNameActivity.this, "Nhập tên tài khoản");
        } else {
            makeRequest(name, phone);
        }
    }


    private void makeRequest(final String user, String phone) {
        showpDialog();

        LoginService apiService = ServiceGenerator.createService(LoginService.class, user, phone);
        Call<CheckUserLoginResult> call = apiService.checkUserLogin();

        call.enqueue(new Callback<CheckUserLoginResult>() {
            @Override
            public void onResponse(Call<CheckUserLoginResult> call, Response<CheckUserLoginResult> response) {

                if (response.body() != null) {
                    if (response.body().getId().equals("0")) {
                        commons.makeToast(LoginNameActivity.this,response.body().getMsg()).show();
                    } else if (response.body().getId().equals("1")) {
                        Intent intent = commons.createIntent(LoginNameActivity.this, LoginPassActivity.class);
                        intent.putExtra(HAIRes.getInstance().KEY_INTENT_USER, user);
                        startActivity(intent);
                        finish();
                    } else if (response.body().getId().equals("2")) {
                        /*
                        Intent intent = commons.createIntent(LoginNameActivity.this, LoginCodeActivity.class);
                        intent.putExtra(HAIRes.getInstance().KEY_INTENT_USER, user);
                        intent.putExtra(HAIRes.getInstance().KEY_INTENT_PHONE, response.body().getPhone());
                        startActivity(intent);
                        finish();*/
                        commons.makeToast(LoginNameActivity.this, "Không dành cho khách hàng").show();
                    } else if (response.body().getId().equals("3")) {
                        //
                        loginSuccess(user, response.body().getToken(), response.body().getType());

                    }
                }

                hidepDialog();
            }

            @Override
            public void onFailure(Call<CheckUserLoginResult> call, Throwable t) {
                hidepDialog();
            }
        });
    }
}
