package com.congtyhai.view;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.congtyhai.adapter.CalendarStatusAdapter;
import com.congtyhai.dms.R;
import com.congtyhai.dms.checkin.CheckInActivity;
import com.congtyhai.model.api.CalendarStatus;
import com.congtyhai.model.api.ResultInfo;
import com.congtyhai.model.api.checkin.CheckInFlexibleSend;
import com.congtyhai.util.HAIRes;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckInFlexibleFragment extends Fragment {


    @BindView(R.id.checkContent)
    EditText eContent;

    @BindView(R.id.checkProvince)
    EditText eProvince;

    @BindView(R.id.checkDistrict)
    EditText eDistrict;

    @BindView(R.id.checkWard)
    EditText eWard;

    @BindView(R.id.btnAdd)
    Button btnAdd;

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    CheckInActivity activity;
    List<CalendarStatus> calendarStatuses;
    Address addressInfo;
    Spinner eStatus;
    public CheckInFlexibleFragment() {

    }

    public void setActivity(CheckInActivity activity, List<CalendarStatus> statuses) {
        this.activity = activity;
        this.calendarStatuses = statuses;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_check_in_flexible, container, false);

        ButterKnife.bind(this, view);

        eStatus = (Spinner) view.findViewById(R.id.estatus);
        createListStatus();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(check()) {
                    CalendarStatus status = calendarStatuses.get(eStatus.getSelectedItemPosition());
                    activity.showpDialog();
                    String muser = activity.prefsHelper.get(HAIRes.getInstance().PREF_KEY_USER, "");
                    String mtoken = activity.prefsHelper.get(HAIRes.getInstance().PREF_KEY_TOKEN, "");

                    CheckInFlexibleSend info = new CheckInFlexibleSend();
                    info.setUser(muser);
                    info.setToken(mtoken);
                    info.setLat(activity.getLat());
                    info.setLng(activity.getLng());
                    String strAddress = addressInfo.getSubThoroughfare() + " " + addressInfo.getThoroughfare();
                    info.setAddress(strAddress);
                    info.setContent(eContent.getText().toString());
                    info.setCountry(addressInfo.getCountryCode());
                    info.setProvince(addressInfo.getAdminArea());
                    info.setDistrict(addressInfo.getSubAdminArea());
                    info.setWard(addressInfo.getLocality());
                    info.setTypeId(status.getId());

                    Call<ResultInfo> call = activity.apiInterface().checkInFlexible(info);

                    call.enqueue(new Callback<ResultInfo>() {
                        @Override
                        public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                           activity.hidepDialog();
                            if (response.body() != null) {
                                if (response.body().getId().equals("1")) {
                                    activity.commons.showAlertInfo(activity, "Thông báo", "Đã cập nhật", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            activity.makeRequest();
                                        }
                                    });
                                } else {
                                    activity.commons.showAlertInfo(activity, "Cảnh báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultInfo> call, Throwable t) {
                            activity.hidepDialog();
                        }
                    });

                }



            }
        });

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new ReadDataTask().execute();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        new ReadDataTask().execute();
        return view;
    }
    private int findPositionSatatus(String status) {
        for (int i = 0; i < calendarStatuses.size(); i++) {
            if(calendarStatuses.get(i).getId().equals(status)) {
                return i;
            }
        }

        return  -1;
    }
    private void createListStatus() {
        CalendarStatusAdapter adapter = new CalendarStatusAdapter(getActivity(), calendarStatuses);
        eStatus.setAdapter(adapter);


        eStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        int idx = findPositionSatatus("CSKH");

        if (idx != -1) {
            eStatus.setSelection(idx);
        }
    }
    private boolean check() {
        if(TextUtils.isEmpty(eContent.getText().toString()))
        {
            eContent.setError("Nhập nội dung");
            return  false;
        }

        if(TextUtils.isEmpty(eProvince.getText().toString())){
            eProvince.setError("Cần tải lại địa chỉ");
            return  false;
        }



        return  true;

    }


    private class ReadDataTask extends AsyncTask<String, Integer, Address> {
        protected Address doInBackground(String... urls) {

            final Geocoder geocoder = new Geocoder(activity);

            final List<Address> addresses;

            try {
                addresses = geocoder.getFromLocation(activity.getLat(),
                        activity.getLng(), 1);

            } catch (IOException e) {
                return null;
            }
            if (addresses != null && !addresses.isEmpty())
                return addresses.get(0);
            else
                return null;

        }

        @Override
        protected void onPreExecute() {
           btnAdd.setEnabled(false);
           btnAdd.setText("Đang lấy địa chỉ...");
        }

        protected void onPostExecute(Address result) {

            if (result != null) {
                addressInfo = result;
              //  String strAddress = result.getSubThoroughfare() + " " + result.getThoroughfare() + " , " + result.getLocality();
                eProvince.setText(result.getAdminArea());
                eDistrict.setText(result.getSubAdminArea());
                eWard.setText(result.getLocality());

                // duong: thro

            }

            btnAdd.setEnabled(true);
            btnAdd.setText("THỰC HIỆN CÔNG VIỆC");
        }

    }
}
