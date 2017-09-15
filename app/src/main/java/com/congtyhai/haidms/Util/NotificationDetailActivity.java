package com.congtyhai.haidms.Util;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.ResultInfo;
import com.congtyhai.util.HAIRes;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationDetailActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView webView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        createToolbar();
        ButterKnife.bind(this);

        Intent intent = getIntent();

        int isRead = intent.getIntExtra("READ",1);

        if (isRead == 0) {
            String id = intent.getStringExtra("ID");
            makeRequest(id);;
        }
        String url = intent.getStringExtra("CONTENT");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setHorizontalScrollBarEnabled(false);

    }

    void makeRequest(String id){
        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        Call<ResultInfo>  call = apiInterface().readNotification(user, id);
        call.enqueue(new Callback<ResultInfo>() {
            @Override
            public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                hidepDialog();
            }

            @Override
            public void onFailure(Call<ResultInfo> call, Throwable t) {
                hidepDialog();
            }
        });
    }

}
