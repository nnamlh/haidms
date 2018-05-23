package com.congtyhai.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.congtyhai.adapter.CheckInAgencyAdapter;
import com.congtyhai.dms.R;
import com.congtyhai.dms.checkin.CheckInActivity;
import com.congtyhai.model.app.CheckInAgencyInfo;
import com.congtyhai.util.HAIRes;
import com.congtyhai.util.HaiActionInterface;

import java.util.ArrayList;
import java.util.List;

public class CheckInPlanFragment extends Fragment {

    RecyclerView recyclerView;
    CheckInAgencyAdapter adapter;

    List<CheckInAgencyInfo> checkInAgencyInfos;
    SwipeRefreshLayout swipeRefreshLayout;
    private CheckInActivity activity;

    public void setActivityCheckIn(CheckInActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_check_in_plan, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
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
                if (info.getDistance() > HAIRes.getInstance().LIMIT_DISTANCE) {
                    Toast.makeText(activity, "Chưa thể checkin", Toast.LENGTH_LONG).show();
                } else {
                    activity.makeTask(info);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        refeshList();

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                       refeshList();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

        return view;
    }

    private void refeshList() {
        checkInAgencyInfos.clear();
        List<CheckInAgencyInfo> temp  = activity.getListCheckInPlan();
        for(CheckInAgencyInfo info: temp) {
            checkInAgencyInfos.add(info);
        }

        adapter.notifyDataSetChanged();
    }

}
