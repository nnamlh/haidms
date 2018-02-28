package com.congtyhai.haidms.manageorders;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import com.congtyhai.adapter.HelpViewPagerAdapter;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.order.OrderProductResult;
import com.congtyhai.model.api.order.UpdateDeliveryResult;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.order.OrderDetailFragment;
import com.congtyhai.view.order.OrderProductFragment;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YourOrderProductActivity extends BaseActivity {

    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    HelpViewPagerAdapter adapter;
    OrderDetailFragment orderDetailView;

    OrderProductFragment orderProductView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_order_product);
        createToolbar();
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Đơn hàng " + HAIRes.getInstance().yourOrderInfo.getCode());

        createTab();

        makeRequest();

    }

    private void createTab() {
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new HelpViewPagerAdapter(getSupportFragmentManager());

        orderProductView = new OrderProductFragment();
        orderProductView.setYourACtivity(this);

        adapter.addFragment(orderProductView, "SẢN PHẨM ĐẶT");

        orderDetailView = new OrderDetailFragment();
        adapter.addFragment(orderDetailView, "CHI TIẾT");

        viewPager.setAdapter(adapter);
    }

    private void makeRequest() {


        showpDialog();

        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");

        Call<List<OrderProductResult>> call = apiInterface().yourOrderProduct(user, HAIRes.getInstance().yourOrderInfo.getOrderId());

        call.enqueue(new Callback<List<OrderProductResult>>() {
            @Override
            public void onResponse(Call<List<OrderProductResult>> call, Response<List<OrderProductResult>> response) {
                if (response.body() != null) {
                    orderProductView.setData(response.body());
                }

                hidepDialog();
            }

            @Override
            public void onFailure(Call<List<OrderProductResult>> call, Throwable t) {
                commons.showToastDisconnect(getApplicationContext());
                hidepDialog();
            }
        });

    }

    public void updateDelivery(final int position, final int quantity, OrderProductResult orderProduct) {

        showpDialog();

        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");

        Call<UpdateDeliveryResult> call = apiInterface().updateDelivery(user, token, quantity, orderProduct.getProductId(), orderProduct.getOrderId());

        call.enqueue(new Callback<UpdateDeliveryResult>() {
            @Override
            public void onResponse(Call<UpdateDeliveryResult> call, Response<UpdateDeliveryResult> response) {
                if(response.body() != null) {
                    HAIRes.getInstance().yourOrderInfo.setDeliveryStatusCode(response.body().getDeliveryStatusCode());
                    HAIRes.getInstance().yourOrderInfo.setDeliveryStatus(response.body().getDeliveryStatus());

                    orderDetailView.udpate();

                    orderProductView.refeshUpdate(position, quantity);

                }

                hidepDialog();
            }

            @Override
            public void onFailure(Call<UpdateDeliveryResult> call, Throwable t) {
                hidepDialog();
                commons.showToastDisconnect(YourOrderProductActivity.this);
            }
        });

    }
}
