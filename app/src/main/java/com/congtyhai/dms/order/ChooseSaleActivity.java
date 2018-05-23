package com.congtyhai.dms.order;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import com.congtyhai.adapter.OrderShowC1Adaper;
import com.congtyhai.dms.BaseActivity;
import com.congtyhai.dms.R;
import com.congtyhai.dms.showinfo.ShowProductActivity;
import com.congtyhai.model.api.SubOwner;
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
    List<SubOwner> c2C1s;

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
        Call<List<SubOwner>> call = apiInterface().orderGetSalePlaces(HAIRes.getInstance().c2Select.getCode(), user);
        call.enqueue(new Callback<List<SubOwner>>() {
            @Override
            public void onResponse(Call<List<SubOwner>> call, Response<List<SubOwner>> response) {

                if(response.body() != null) {

                    c2C1s = response.body();

                    mC1Adapter = new OrderShowC1Adaper(ChooseSaleActivity.this, c2C1s);

                    sC1Choose.setAdapter(mC1Adapter);


                }

                hidepDialog();
            }

            @Override
            public void onFailure(Call<List<SubOwner>> call, Throwable t) {
                commons.showToastDisconnect(ChooseSaleActivity.this);
                hidepDialog();
            }
        });


    }

}
