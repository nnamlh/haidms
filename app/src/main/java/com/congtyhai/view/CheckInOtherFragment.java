package com.congtyhai.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.congtyhai.adapter.CalendarStatusAdapter;
import com.congtyhai.adapter.CheckInAgencyAdapter;
import com.congtyhai.haidms.Agency.ShowAgencyActivity;
import com.congtyhai.haidms.Agency.ShowAgencyDetailActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.haidms.calendar.CreateCalendarActivity;
import com.congtyhai.haidms.checkin.CheckInActivity;
import com.congtyhai.model.api.CalendarStatus;
import com.congtyhai.model.app.CheckInAgencyInfo;
import com.congtyhai.util.HAIRes;
import com.congtyhai.util.HaiActionInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class CheckInOtherFragment extends Fragment {

    RecyclerView recyclerView;
    CheckInAgencyAdapter adapter;

    List<CheckInAgencyInfo> checkInAgencyInfos;
    SwipeRefreshLayout swipeRefreshLayout;

    private CheckInActivity activity;

    public void setActivityCheckIn(CheckInActivity activity, List<CalendarStatus> statuses) {
        this.activity = activity;
        this.calendarStatuses = statuses;
    }

    Spinner eStatus;

    EditText eSearch;
    Button btnSend;

    List<CalendarStatus> calendarStatuses;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_check_in_other, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        eStatus = (Spinner) view.findViewById(R.id.estatus);
        eSearch = (EditText) view.findViewById(R.id.esearch);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        btnSend = (Button) view.findViewById(R.id.btnsend);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        checkInAgencyInfos = new ArrayList<>();
        adapter = new CheckInAgencyAdapter(checkInAgencyInfos);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                CheckInAgencyInfo info = checkInAgencyInfos.get(position);
                /*
                if (info.getDistance() > HAIRes.getInstance().LIMIT_DISTANCE) {
                    Toast.makeText(activity, "Chưa thể checkin", Toast.LENGTH_LONG).show();
                }else {
                    activity.makeTask(info);
                }*/
                CalendarStatus status = calendarStatuses.get(eStatus.getSelectedItemPosition());
                activity.createOutPlan(info.getCode(), status.getId(), status.getName());
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarStatus status = calendarStatuses.get(eStatus.getSelectedItemPosition());
                activity.createOutPlan("", status.getId(), status.getName());
            }
        });

        refeshList("");

        createListStatus();
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        refeshList(eSearch.getText().toString());
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );


        eSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                refeshList(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void refeshList(String search) {
        checkInAgencyInfos.clear();
        List<CheckInAgencyInfo> temp = activity.getListCheckOutPlan(search);
        for(CheckInAgencyInfo info: temp) {
            checkInAgencyInfos.add(info);
        }

        adapter.notifyDataSetChanged();

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
    }

}
