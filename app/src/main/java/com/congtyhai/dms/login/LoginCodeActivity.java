package com.congtyhai.dms.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.congtyhai.dms.R;
import com.congtyhai.model.api.LoginResult;
import com.congtyhai.util.HAIRes;
import com.congtyhai.util.LoginService;
import com.congtyhai.util.ServiceGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginCodeActivity extends LoginActivity {


    @BindView(R.id.txtstatus)
    TextView txtStatus;

    @BindView(R.id.ecode)
    EditText eCode;

    String user = "";
    String phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_code);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        user = intent.getStringExtra(HAIRes.getInstance().KEY_INTENT_USER);
        phone = intent.getStringExtra(HAIRes.getInstance().KEY_INTENT_PHONE);

        txtStatus.setText("Đăng nhập với tài khoản: " + user + "\n" + "Một mã kích hoạt được gửi đến số: " + phone);
    }

    public void loginClick(View view) {
        String code = eCode.getText().toString();
        if (TextUtils.isEmpty(code)) {
            commons.makeToast(getApplicationContext(),"Nhập mã kích hoặt").show();
        } else {
            loginCheck(user,code );
        }
    }

    private void loginCheck(String name, String code) {
        showpDialog();

        LoginService apiService = ServiceGenerator.createService(LoginService.class, name, code);

        Call<LoginResult> call = apiService.loginActivaton();

        call.enqueue(new Callback<LoginResult>() {

            @Override
            public void onResponse(Call<LoginResult> call, retrofit2.Response<LoginResult> response) {
                hidepDialog();

                if (response.body() != null){
                    if ("1".equals(response.body().getId())) {
                        loginSuccess(user, response.body().getToken(), response.body().getType());
                    } else {
                        commons.makeToast(getApplicationContext(), response.body().getMsg()).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                hidepDialog();
            }
        });
    }
}
