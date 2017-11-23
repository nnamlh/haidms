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
import android.text.TextUtils;

import com.congtyhai.adapter.AgencyAdapter;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.AgencyInfo;
import com.congtyhai.model.api.CalendarStatus;
import com.congtyhai.model.api.CheckInGetPlanResult;
import com.congtyhai.model.api.CheckInGetPlanSend;
import com.congtyhai.model.api.ResultInfo;
import com.congtyhai.model.api.checkin.AgencyCheckinInfo;
import com.congtyhai.model.api.checkin.CheckInOutPlanSend;
import com.congtyhai.model.app.C2Info;
import com.congtyhai.model.app.CheckInAgencyInfo;
import com.congtyhai.util.HAIRes;
import com.congtyhai.view.CheckInOtherFragment;
import com.congtyhai.view.CheckInPlanFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckInActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<AgencyCheckinInfo> checkInLists;
    private List<AgencyInfo> agencyInfos;
    private List<CalendarStatus> statuses;
    int SHOW_TASK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        createToolbar();
        statuses = new ArrayList<>();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        checkInLists = new ArrayList<>();
        // outPlans = new ArrayList<>();
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
                    checkInLists.clear();
                    //   outPlans.clear();
                    if (response.body().getId().equals("0")) {
                        commons.showAlertInfo(CheckInActivity.this, "Cảnh báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    } else {
                        checkInLists = response.body().getCheckin();
                        //outPlans = response.body().getOutplan();
                        statuses = response.body().getStatus();
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

    public void createOutPlan(final String code, final String cType, String cName) {
       if (TextUtils.isEmpty(code)) {
           commons.showAlertCancel(CheckInActivity.this, "Thông báo", "Bạn đang thêm lịch ngoài kế hoạch nhưng không ghé thăm khách hàng: " + cName , new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                    sendOutPlan(code, cType);
               }
           });
       } else {
           commons.showAlertCancel(CheckInActivity.this, "Thông báo", "Bạn đang thêm lịch ngoài kế hoạch : " + cName + ", cho khách hàng: " + code, new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   sendOutPlan(code, cType);
               }
           });
       }
    }

    private void sendOutPlan(String code, String cType) {
        showpDialog();
        String user = prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
        String token = prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");
        CheckInOutPlanSend info = new CheckInOutPlanSend();
        info.setCode(code);
        info.setCtype(cType);
        info.setToken(token);
        info.setUser(user);
        info.setLat(getCurrentLocation().getLatitude());
        info.setLng(getCurrentLocation().getLongitude());

        Call<ResultInfo> call = apiInterface().checkInOutPlan(info);
        call.enqueue(new Callback<ResultInfo>() {
            @Override
            public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                if (response.body() != null) {
                    if (response.body().getId().equals("1")) {
                        commons.showAlertInfo(CheckInActivity.this, "Thông báo", "Đã tạo xong kế hoạch ngoài, vào tab DANH SÁCH GHÉ THĂM để thực hiện ghé thăm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                makeRequest();
                            }
                        });
                    } else {
                        commons.showAlertInfo(CheckInActivity.this, "Cảnh báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    }
                }

                hidepDialog();
            }

            @Override
            public void onFailure(Call<ResultInfo> call, Throwable t) {
                commons.showToastDisconnect(CheckInActivity.this);
                hidepDialog();
            }
        });
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
        for (AgencyCheckinInfo item : checkInLists) {
            CheckInAgencyInfo checkInAgencyInfo = new CheckInAgencyInfo();
            checkInAgencyInfo.setDeputy(item.getDeputy());
            checkInAgencyInfo.setCode(item.getCode());
            checkInAgencyInfo.setName(item.getName());
            checkInAgencyInfo.setCheckInName(item.getCname());
            checkInAgencyInfo.setCheckInType(item.getCtype());
            checkInAgencyInfo.setIsShowType(1);
            checkInAgencyInfo.setInPlan(item.getInPlan());
            float distabce = commons.distance(lat, lng, item.getLat(), item.getLng());
            checkInAgencyInfo.setDistance(distabce);
            checkInAgencyInfos.add(checkInAgencyInfo);
        }
        return checkInAgencyInfos;
    }

    public List<CheckInAgencyInfo> getListCheckOutPlan(String search) {
        List<CheckInAgencyInfo> checkInAgencyInfos = new ArrayList<>();
        double lat = getCurrentLocation().getLatitude();
        double lng = getCurrentLocation().getLongitude();
        for (AgencyInfo item : agencyInfos) {

           if((item.getCode().contains(search) || item.getName().contains(search)) && !checkInPlan(item.getCode())) {
               CheckInAgencyInfo checkInAgencyInfo = new CheckInAgencyInfo();
               checkInAgencyInfo.setDeputy(item.getDeputy());
               checkInAgencyInfo.setCode(item.getCode());
               checkInAgencyInfo.setName(item.getName());
               checkInAgencyInfo.setIsShowType(0);
               float distabce = commons.distance(lat, lng, item.getLat(), item.getLng());
               checkInAgencyInfo.setDistance(distabce);
               checkInAgencyInfos.add(checkInAgencyInfo);
           }
        }
        return checkInAgencyInfos;
    }

    private boolean checkInPlan(String code) {
        for(AgencyCheckinInfo item : checkInLists) {
            if(item.getCode().equals(code)) {
                return  true;
            }
        }

        return  false;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        CheckInPlanFragment inPlanFragment = new CheckInPlanFragment();
        inPlanFragment.setActivityCheckIn(CheckInActivity.this);
        adapter.addFragment(inPlanFragment, "DANH SÁCH GHÉ THĂM");


        CheckInOtherFragment otherFragment = new CheckInOtherFragment();
        otherFragment.setActivityCheckIn(CheckInActivity.this, statuses);
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
