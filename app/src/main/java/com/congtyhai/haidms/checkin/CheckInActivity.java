package com.congtyhai.haidms.checkin;

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
import com.congtyhai.model.api.CheckInResult;
import com.congtyhai.model.api.CheckInSend;
import com.congtyhai.model.app.CheckInAgencyInfo;
import com.congtyhai.util.HAIRes;
import com.congtyhai.util.HaiActionInterface;
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
    public static List<String> inPlans = new ArrayList<>();
    public static List<String> outPlans = new ArrayList<>();
    private static List<AgencyInfo> agencyInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        createToolbar();
        createLocation();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        makeRequest();

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
                    inPlans = response.body().getInplan();
                    outPlans = response.body().getOutplan();
                }


                hidepDialog();
                new ReadDataTask().execute();
            }

            @Override
            public void onFailure(Call<CheckInGetPlanResult> call, Throwable t) {
                hidepDialog();
            }
        });

    }

    public void makeUpdate(String code, long distance, final int inPlan, final HaiActionInterface action) {
        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
        CheckInSend checkInSend = new CheckInSend();
        checkInSend.setCode(code);
        checkInSend.setUser(user);
        checkInSend.setToken(token);
        checkInSend.setDistance(distance);
        checkInSend.setInPlan(inPlan);
        checkInSend.setLat(getCurrentLocation().getLatitude());
        checkInSend.setLng(getCurrentLocation().getLongitude());

        Call<CheckInResult> call = apiInterface().checkIn(checkInSend);
        call.enqueue(new Callback<CheckInResult>() {
            @Override
            public void onResponse(Call<CheckInResult> call, Response<CheckInResult> response) {
                if (response.body() != null) {
                    if (inPlan == 1) {
                        inPlans = response.body().getNewplan();
                    } else {
                        outPlans = response.body().getNewplan();
                    }

                }
                action.onResult();
                hidepDialog();
            }

            @Override
            public void onFailure(Call<CheckInResult> call, Throwable t) {
                hidepDialog();
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
        for(String item : inPlans) {
            AgencyInfo info = findAgency(item);
            if (info != null) {
                CheckInAgencyInfo checkInAgencyInfo = new CheckInAgencyInfo();
                checkInAgencyInfo.setDeputy(info.getDeputy());
                checkInAgencyInfo.setCode(info.getCode());
                checkInAgencyInfo.setName(info.getName());
                long distabce = commons.distance(getCurrentLocation().getLatitude(), getCurrentLocation().getLongitude(), info.getLat(), info.getLng(), "M");
                checkInAgencyInfo.setDistance(distabce);

                checkInAgencyInfos.add(checkInAgencyInfo);
            }
        }
        return checkInAgencyInfos;
    }

    public List<CheckInAgencyInfo> getListCheckOutPlan() {
        List<CheckInAgencyInfo> checkInAgencyInfos = new ArrayList<>();
        for(AgencyInfo item : agencyInfos) {

            if (!outPlans.contains(item.getCode())) {
                CheckInAgencyInfo checkInAgencyInfo = new CheckInAgencyInfo();
                checkInAgencyInfo.setDeputy(item.getDeputy());
                checkInAgencyInfo.setCode(item.getCode());
                checkInAgencyInfo.setName(item.getName());
                long distabce = commons.distance(getCurrentLocation().getLatitude(), getCurrentLocation().getLongitude(), item.getLat(), item.getLng(), "M");
                checkInAgencyInfo.setDistance(distabce);

                checkInAgencyInfos.add(checkInAgencyInfo);
            }

        }
        return checkInAgencyInfos;
    }

    public AgencyInfo findAgency(String code){
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
        adapter.addFragment(otherFragment, "KHÁC");
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
