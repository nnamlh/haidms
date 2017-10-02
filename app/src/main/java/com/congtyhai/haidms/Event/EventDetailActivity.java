package com.congtyhai.haidms.Event;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.congtyhai.adapter.EventProductAdapter;
import com.congtyhai.adapter.GiftAdapter;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.AwardInfo;
import com.congtyhai.model.api.EventInfoSend;
import com.congtyhai.model.api.EventProduct;
import com.congtyhai.model.api.ResultEventDetail;
import com.congtyhai.util.ApiInterface;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.NonScrollListView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailActivity extends BaseActivity {

    private ImageView thumbnail;
    private TextView txtName;
    private TextView txtDescibe;
    private TextView txtTime;
    private NonScrollListView listView;
    private GiftAdapter adapterGift;
    private List<AwardInfo> awardInfos;

    private NonScrollListView listProduct;
    private EventProductAdapter eventProductAdapter;
    private List<EventProduct> eventProducts;

    private String eventId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        createToolbar();

        HAIRes.getInstance().resetListProductEvent();
        // dialog
        initCollapsingToolbar();

        thumbnail = (ImageView) findViewById(R.id.thumbnail);
        txtName = (TextView) findViewById(R.id.event_name);
        txtDescibe = (TextView) findViewById(R.id.event_describe);
        txtTime = (TextView) findViewById(R.id.event_time);
        listView = (NonScrollListView) findViewById(R.id.event_gift);
        awardInfos = new ArrayList<>();
        adapterGift = new GiftAdapter(this, awardInfos);
        listView.setAdapter(adapterGift);


        listProduct = (NonScrollListView) findViewById(R.id.event_product);
        eventProducts = new ArrayList<>();
        eventProductAdapter = new EventProductAdapter(this, eventProducts);
        listProduct.setAdapter(eventProductAdapter);


        Intent intent = getIntent();
        eventId = intent.getStringExtra("EventID");

        prepareData(eventId);


    }
    public void sendClick (View view) {
      //  Intent intent = new Intent(EventDetailActivity.this, EventSendActivity.class);
    //    intent.putExtra("EventID", eventId);
       // startActivity(intent);
    }

    private void prepareData(String eventId) {

        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
        EventInfoSend info = new EventInfoSend(user, token, eventId);

        Call<ResultEventDetail> call = apiInterface().eventDetail(info);

        call.enqueue(new Callback<ResultEventDetail>() {
            @Override
            public void onResponse(Call<ResultEventDetail> call,
                                   Response<ResultEventDetail> response) {

                if (response.body() != null) {
                    if ("1".equals(response.body().getId())) {
                        txtName.setText(response.body().getEname());
                        txtTime.setText(response.body().getEtime());
                        txtDescibe.setText(response.body().getEdescribe());
                        Glide.with(EventDetailActivity.this).load(response.body().getEimage()).into(thumbnail);

                        for(AwardInfo info : response.body().getAwards()) {
                            awardInfos.add(info);
                        }
                        adapterGift.notifyDataSetChanged();

                        int count = 1;

                        for(EventProduct info: response.body().getProducts()) {
                            HAIRes.getInstance().addListProductEvent(info);

                            if (count <= 5) {
                                eventProducts.add(info);
                                count++;
                            }
                        }
                        eventProductAdapter.notifyDataSetChanged();

                    } else {
                        commons.showAlertInfo(EventDetailActivity.this,"Cảnh báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    }
                }

                hidepDialog();
            }

            @Override
            public void onFailure(Call<ResultEventDetail> call, Throwable t) {
                hidepDialog();
                commons.showAlertInfo(EventDetailActivity.this,"Cảnh báo", "Đường truyền bị lỗi, kiểm tra lại kết nối wifi hoặc 3G.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }
        });


    }

    public void onShowMoreClick(View view) {
        Intent intent = new Intent(EventDetailActivity.this, ProductEventDetailActivity.class);
        startActivity(intent);
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.title_activity_event_detail));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

}
