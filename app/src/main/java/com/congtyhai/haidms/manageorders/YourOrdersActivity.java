package com.congtyhai.haidms.manageorders;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;

import butterknife.ButterKnife;

public class YourOrdersActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_orders);
        createToolbar();

        ButterKnife.bind(this);
    }

}
