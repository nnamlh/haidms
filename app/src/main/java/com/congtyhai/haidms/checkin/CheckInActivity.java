package com.congtyhai.haidms.checkin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.AgencyInfo;
import com.congtyhai.model.api.CheckInGetPlanResult;
import com.congtyhai.model.api.CheckInGetPlanSend;
import com.congtyhai.model.app.C2Info;
import com.congtyhai.model.app.CheckInAgencyInfo;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.CheckInOtherFragment;
import com.congtyhai.view.CheckInPlanFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckInActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> inPlans;
    private List<String> outPlans;
    private List<AgencyInfo> agencyInfos;
    int SHOW_TASK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        createToolbar();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        inPlans = new ArrayList<>();
        outPlans = new ArrayList<>();
        agencyInfos = new ArrayList<>();
        makeRequest();

        createLocation();

    }

    private void makeRequest() {
        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        CheckInGetPlanSend info = new CheckInGetPlanSend();
        info.setUser(user);
        info.setToken(token);
        info.setMonth(calendar.get(Calendar.MONTH) + 1);
        info.setYear(calendar.get(Calendar.YEAR));
        info.setDay(calendar.get(Calendar.DATE));
        Call<CheckInGetPlanResult> call = apiInterface().checkInGetPlan(info);

        call.enqueue(new Callback<CheckInGetPlanResult>() {
            @Override
            public void onResponse(Call<CheckInGetPlanResult> call, Response<CheckInGetPlanResult> response) {
                if (response.body() != null) {
                    inPlans.clear();
                    outPlans.clear();
                    if (response.body().getId().equals("0")) {
                        commons.showAlertInfo(CheckInActivity.this, "Cảnh báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    } else {
                        inPlans = response.body().getInplan();
                        outPlans = response.body().getOutplan();
                        new ReadDataTask().execute();
                    }
                }


                hidepDialog();

            }

            @Override
            public void onFailure(Call<CheckInGetPlanResult> call, Throwable t) {
                hidepDialog();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SHOW_TASK) {
            if (resultCode == RESULT_OK) {
                makeRequest();
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    public void makeTask(final CheckInAgencyInfo info) {
        commons.showAlertCancel(CheckInActivity.this, "Thông báo", "Bạn muốn ghé thăm khách hàng: " + info.getName() + " - " + info.getCode(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = commons.createIntent(CheckInActivity.this, CheckInTaskActivity.class);
                intent.putExtra(HAIRes.getInstance().KEY_INTENT_AGENCY_CODE, info.getCode());
                intent.putExtra(HAIRes.getInstance().KEY_INTENT_TEMP, info.getDistance());

                // save aency
                C2Info c2Info = new C2Info();
                c2Info.setCode(info.getCode());
                c2Info.setDeputy(info.getDeputy());
                c2Info.setStore(info.getName());
                c2Info.setC1(info.getC1());

                HAIRes.getInstance().c2Select = c2Info;

                startActivityForResult(intent, SHOW_TASK);
            }
        });
    }

    private class ReadDataTask extends AsyncTask<String, Integer, List<AgencyInfo>> {
        protected List<AgencyInfo> doInBackground(String... urls) {

            List<AgencyInfo> data = new ArrayList<>();
            try {

                data = getListAgency();

            } catch (Exception e) {

            }
            return data;
        }

        @Override
        protected void onPreExecute() {
            showpDialog();
        }

        protected void onPostExecute(List<AgencyInfo> result) {
            agencyInfos = result;
            createTab();
            hidepDialog();
        }
    }

    private void createTab() {
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    public List<CheckInAgencyInfo> getListCheckInPlan() {
        List<CheckInAgencyInfo> checkInAgencyInfos = new ArrayList<>();
        double lat = getCurrentLocation().getLatitude();
        double lng = getCurrentLocation().getLongitude();
        for (String item : inPlans) {
            AgencyInfo info = findAgency(item);
            if (info != null) {
                CheckInAgencyInfo checkInAgencyInfo = new CheckInAgencyInfo();
                checkInAgencyInfo.setDeputy(info.getDeputy());
                checkInAgencyInfo.setCode(info.getCode());
                checkInAgencyInfo.setName(info.getName());
                checkInAgencyInfo.setC1(info.getC1());
                float distabce = commons.distance(lat, lng, info.getLat(), info.getLng());
                checkInAgencyInfo.setDistance(distabce);

                checkInAgencyInfos.add(checkInAgencyInfo);
            }
        }
        return checkInAgencyInfos;
    }

    public List<CheckInAgencyInfo> getListCheckOutPlan() {
        List<CheckInAgencyInfo> checkInAgencyInfos = new ArrayList<>();
        double lat = getCurrentLocation().getLatitude();
        double lng = getCurrentLocation().getLongitude();
        for (AgencyInfo item : agencyInfos) {

            if (!outPlans.contains(item.getCode())) {
                CheckInAgencyInfo checkInAgencyInfo = new CheckInAgencyInfo();
                checkInAgencyInfo.setDeputy(item.getDeputy());
                checkInAgencyInfo.setCode(item.getCode());
                checkInAgencyInfo.setName(item.getName());
                checkInAgencyInfo.setC1(item.getC1());
                float distabce = commons.distance(lat, lng, item.getLat(), item.getLng());
                checkInAgencyInfo.setDistance(distabce);

                checkInAgencyInfos.add(checkInAgencyInfo);
            }

        }
        return checkInAgencyInfos;
    }

    public AgencyInfo findAgency(String code) {
        for (AgencyInfo agency : agencyInfos) {
            if (agency.getCode().equals(code))
                return agency;
        }

        return null;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        CheckInPlanFragment inPlanFragment = new CheckInPlanFragment();
        inPlanFragment.setActivityCheckIn(CheckInActivity.this);
        adapter.addFragment(inPlanFragment, "KẾ HOẠCH NGÀY");


        CheckInOtherFragment otherFragment = new CheckInOtherFragment();
        otherFragment.setActivityCheckIn(CheckInActivity.this);
        adapter.addFragment(otherFragment, "NGOÀI KẾ HOẠCH");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
