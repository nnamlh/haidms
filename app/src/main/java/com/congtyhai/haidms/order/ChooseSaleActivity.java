package com.congtyhai.haidms.order;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import com.congtyhai.adapter.OrderShowC1Adaper;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.haidms.showinfo.ShowProductActivity;
import com.congtyhai.model.api.AgencyC2C1;
import com.congtyhai.util.HAIRes;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseSaleActivity extends BaseActivity {

    @BindView(R.id.sc1choose)
    Spinner sC1Choose;
    OrderShowC1Adaper mC1Adapter;
    List<AgencyC2C1> c2C1s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sale);
        createToolbar();
        ButterKnife.bind(this);

        makeRequest();

    }

    public void nextClick(View view) {
        int idx = sC1Choose.getSelectedItemPosition();
        HAIRes.getInstance().salePlace = c2C1s.get(idx);

        commons.startActivity(ChooseSaleActivity.this, ShowProductActivity.class);
    }

    private void makeRequest() {
        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        Call<List<AgencyC2C1>> call = apiInterface().orderGetSalePlaces(HAIRes.getInstance().c2Select.getCode(), user);
        call.enqueue(new Callback<List<AgencyC2C1>>() {
            @Override
            public void onResponse(Call<List<AgencyC2C1>> call, Response<List<AgencyC2C1>> response) {

                if(response.body() != null) {

                    c2C1s = response.body();

                    mC1Adapter = new OrderShowC1Adaper(ChooseSaleActivity.this, c2C1s);

                    sC1Choose.setAdapter(mC1Adapter);


                }

                hidepDialog();
            }

            @Override
            public void onFailure(Call<List<AgencyC2C1>> call, Throwable t) {
                commons.showToastDisconnect(ChooseSaleActivity.this);
                hidepDialog();
            }
        });


    }

}
