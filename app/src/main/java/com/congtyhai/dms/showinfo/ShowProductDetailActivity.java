package com.congtyhai.dms.showinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.congtyhai.dms.BaseActivity;
import com.congtyhai.dms.R;
import com.congtyhai.model.api.ProductDetailResult;
import com.congtyhai.util.HAIRes;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowProductDetailActivity extends BaseActivity {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.group)
    TextView group;
    @BindView(R.id.code)
    TextView code;
    @BindView(R.id.active)
    TextView active;
    @BindView(R.id.producer)
    TextView producer;
    @BindView(R.id.register)
    TextView register;
    @BindView(R.id.describe)
    TextView describe;
    @BindView(R.id.introduce)
    TextView introduce;
    @BindView(R.id.notes)
    TextView notes;
    @BindView(R.id.uses)
    TextView uses;
    @BindView(R.id.other)
    TextView other;

    @BindView(R.id.carouselView)
    CarouselView carouselView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product_detail);
        createToolbar();
        ButterKnife.bind(this);
        Intent intent = getIntent();
        makeRequest(intent.getStringExtra(HAIRes.getInstance().KEY_INTENT_TEMP));

    }

    private void makeRequest(String id) {

        showpDialog();
        final String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");

        Call<ProductDetailResult> call = apiInterface().getProductDetail(user, token, id);
        call.enqueue(new Callback<ProductDetailResult>() {
            @Override
            public void onResponse(Call<ProductDetailResult> call, final Response<ProductDetailResult> response) {
                hidepDialog();
                if (response.body() != null) {
                    name.setText(response.body().getName());
                    code.setText("Mã: " + response.body().getCode());
                    group.setText("Nhóm: " + response.body().getGroupName());
                    active.setText("Hoặt chất: " + response.body().getActivce());
                    producer.setText("Nhà sản xuất: " + response.body().getProducer());
                    register.setText("Nhà phân phối: " + response.body().getRegister());
                    describe.setText(response.body().getDescribe());
                    notes.setText(response.body().getNotes());
                    uses.setText(response.body().getUses());
                    introduce.setText(response.body().getIntroduce());
                    other.setText(response.body().getOther());

                    if(response.body().getImages() != null) {
                        int size = response.body().getImages().size();

                        carouselView.setViewListener(new ViewListener() {
                            @Override
                            public View setViewForPosition(int position) {

                                View customView = getLayoutInflater().inflate(R.layout.carousel_product_image, null);
                                ImageView imageView = (ImageView) customView.findViewById(R.id.image);
                                Glide.with(ShowProductDetailActivity.this).load(response.body().getImages().get(position))
                                        .thumbnail(0.5f)
                                        .into(imageView);
                                return customView;
                            }
                        });

                        carouselView.setPageCount(size);

                    }
                }
            }

            @Override
            public void onFailure(Call<ProductDetailResult> call, Throwable t) {
                hidepDialog();
            }
        });
    }

}
