package com.congtyhai.haidms.order;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.congtyhai.adapter.HelpViewPagerAdapter;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.ProductOrder;
import com.congtyhai.model.api.ResultInfo;
import com.congtyhai.model.api.order.OrderCompleteSend;
import com.congtyhai.model.api.order.OrderConfirmResult;
import com.congtyhai.model.api.order.OrderConfirmSend;
import com.congtyhai.model.api.order.OrderProductSend;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.CompleteOrderFragment;
import com.congtyhai.view.CompleteOrderPromotionFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteOrderActivity extends BaseActivity {

    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    HelpViewPagerAdapter adapter;

    CompleteOrderFragment order;

    CompleteOrderPromotionFragment promotion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_order);
        createToolbar();
        ButterKnife.bind(this);
        makeRequest();


    }
    private void createTab(OrderConfirmResult result) {
        setupViewPager(viewPager, result);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager, OrderConfirmResult result) {
         adapter = new HelpViewPagerAdapter(getSupportFragmentManager());

        order = new CompleteOrderFragment();
        order.setData(CompleteOrderActivity.this, result.getDeputy(), result.getStore(), result.getAgencyCode(),result.getPhone(), result.getAddress(), result.getPayType(), result.getShipType());
        adapter.addFragment(order, "ĐẶT HÀNG");

        promotion = new CompleteOrderPromotionFragment();
        //
        promotion.setData(result.getEvents());

        adapter.addFragment(promotion, "KHUYẾN MÃI");
        viewPager.setAdapter(adapter);
    }

    public void hidePromote(boolean isHide) {
        if (!isHide) {
            adapter.removeFragment(1);
            adapter.notifyDataSetChanged();
        } else{
            adapter.addFragment(promotion, "KHUYẾN MÃI");
            adapter.notifyDataSetChanged();
        }
    }

    private void makeRequest() {
        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");

        OrderConfirmSend info  = new OrderConfirmSend();
        info.setUser(user);
        info.setToken(token);
        info.setAgency(HAIRes.getInstance().c2Select.getCode());
        info.setProduct(new ArrayList<OrderProductSend>());

        for(ProductOrder productOrder: HAIRes.getInstance().getProductOrder()){
            OrderProductSend orderProductSend = new OrderProductSend();
            orderProductSend.setCode(productOrder.getCode());
            orderProductSend.setQuantity(productOrder.getQuantity());
            info.getProduct().add(orderProductSend);
        }

        Call<OrderConfirmResult> call = apiInterface().orderConfirm(info);
        call.enqueue(new Callback<OrderConfirmResult>() {
            @Override
            public void onResponse(Call<OrderConfirmResult> call, Response<OrderConfirmResult> response) {

                if(response.body() != null) {
                    createTab(response.body());
                }

                hidepDialog();
            }

            @Override
            public void onFailure(Call<OrderConfirmResult> call, Throwable t) {
                commons.showToastDisconnect(CompleteOrderActivity.this);
                hidepDialog();
            }
        });
    }

    //
    public  void makeUpdate( String address, String phone, String note, String shipType, String payType, String timeSuggest, String c1, int debtTime) {
        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");

        OrderCompleteSend info = new OrderCompleteSend();

        info.setProduct(new ArrayList<OrderProductSend>());
        for(ProductOrder productOrder: HAIRes.getInstance().getProductOrder()){
            OrderProductSend orderProductSend = new OrderProductSend();
            orderProductSend.setCode(productOrder.getCode());
            orderProductSend.setQuantity(productOrder.getQuantity());
            orderProductSend.setHasBill(productOrder.getHasBill());
            info.getProduct().add(orderProductSend);
        }

        info.setUser(user);
        info.setToken(token);
        info.setCode(HAIRes.getInstance().c2Select.getCode());
        info.setAddress(address);
        info.setPhone(phone);
        info.setNotes(note);
        info.setShipType(shipType);
        info.setPayType(payType);
        info.setTimeSuggest(timeSuggest);
        info.setC1(c1);
        info.setInCheckIn(HAIRes.getInstance().CREATE_ORDER_TYPE);
        info.setDebtTime(debtTime);

        Call<ResultInfo> call = apiInterface().orderComplete(info);
        call.enqueue(new Callback<ResultInfo>() {
            @Override
            public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                if (response.body() != null){
                    hidepDialog();
                    if (response.body().getId().equals("1")){
                        commons.showAlertInfo(CompleteOrderActivity.this, "Thông báo", "Đơn hàng đã được tạo, vào phần quản lý đơn hàng để theo dõi tình trạng", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                HAIRes.getInstance().getProductOrder().clear();
                                finish();
                            }
                        });
                    } else{
                        commons.showAlertInfo(CompleteOrderActivity.this, "Thông báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultInfo> call, Throwable t) {
                hidepDialog();
                commons.showToastDisconnect(CompleteOrderActivity.this);
            }
        });
    }
}
