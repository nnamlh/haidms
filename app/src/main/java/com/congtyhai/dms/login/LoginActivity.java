package com.congtyhai.dms.login;

import android.content.Intent;
import android.os.Bundle;
import com.congtyhai.dms.BaseActivity;
import com.congtyhai.dms.MainActivity;
import com.congtyhai.util.HAIRes;
import com.congtyhai.util.RealmController;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    protected void loginSuccess(String user, String token, String type) {

        String oldUser = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");

        if (oldUser != null && !oldUser.equals(user)) {
            // delete all old data
            RealmController.getInstance().clearAll();
        }

        prefsHelper.put(HAIRes.getInstance().PREF_KEY_USER, user);
        prefsHelper.put(HAIRes.getInstance().PREF_KEY_TOKEN, token);
        prefsHelper.put(HAIRes.getInstance().PREF_KEY_TYPE, type);

       if ("STAFF".equals(type)) {
           Intent intent = new Intent(LoginActivity.this, MainActivity.class);
           startActivity(intent);

       }
        finish();
    }

}
