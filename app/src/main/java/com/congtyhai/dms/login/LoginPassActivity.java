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

public class LoginPassActivity extends LoginActivity {

    @BindView(R.id.epass)
    EditText ePass;

    @BindView(R.id.txtstatus)
    TextView txtStatus;

    String user = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pass);
        ButterKnife.bind(this);
        Intent intent = getIntent();

        user = intent.getStringExtra(HAIRes.getInstance().KEY_INTENT_USER);

        txtStatus.setText("Đăng nhập với tài khoản: " + user);

    }


    public void loginClick(View view) {
        String pass = ePass.getText().toString();
        if (TextUtils.isEmpty(pass)) {
            commons.makeToast(getApplicationContext(),"Nhập mật khẩu").show();
        } else {
            loginCheck(user, pass);
        }
    }

    private void loginCheck(String name, String pass) {
       showpDialog();

        LoginService apiService = ServiceGenerator.createService(LoginService.class, name, pass);

        Call<LoginResult> call = apiService.basicLogin("");

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
