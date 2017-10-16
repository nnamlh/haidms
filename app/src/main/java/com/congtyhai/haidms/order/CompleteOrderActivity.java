package com.congtyhai.haidms.order;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import com.congtyhai.adapter.HelpViewPagerAdapter;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.view.CompleteOrderFragment;
import com.congtyhai.view.CompleteOrderPromotionFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CompleteOrderActivity extends BaseActivity {

    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_order);
        createToolbar();
        ButterKnife.bind(this);

        createLocation();
        createTab();

    }
    private void createTab() {
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        HelpViewPagerAdapter adapter = new HelpViewPagerAdapter(getSupportFragmentManager());
        CompleteOrderFragment order = new CompleteOrderFragment();
        adapter.addFragment(order, "ĐẶT HÀNG");


        CompleteOrderPromotionFragment promotion = new CompleteOrderPromotionFragment();
        adapter.addFragment(promotion, "KHUYẾN MÃI");
        viewPager.setAdapter(adapter);
    }


}
