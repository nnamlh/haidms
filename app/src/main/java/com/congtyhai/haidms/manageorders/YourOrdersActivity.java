package com.congtyhai.haidms.manageorders;

import android.os.Bundle;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;

import butterknife.ButterKnife;

public class YourOrdersActivity extends BaseActivity {

    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_orders);
        createToolbar();

        ButterKnife.bind(this);
    }

}
