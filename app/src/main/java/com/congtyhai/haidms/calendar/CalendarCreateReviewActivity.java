package com.congtyhai.haidms.calendar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.congtyhai.adapter.CalendarCreateReviewAdapter;
import com.congtyhai.haidms.BaseActivity;
import com.congtyhai.haidms.R;
import com.congtyhai.model.api.AgencyInfo;
import com.congtyhai.model.api.CalendarCreateSend;
import com.congtyhai.model.api.CalendarDayCreate;
import com.congtyhai.model.app.CalendarCreateReviewInfo;
import com.congtyhai.util.HAIRes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarCreateReviewActivity extends BaseActivity {

    @BindView(R.id.list)
    ListView listView;
    private List<AgencyInfo> agencyList = new ArrayList<>();
    List<CalendarCreateReviewInfo> calendarCreateReviewInfoList = new ArrayList<>();
    CalendarCreateReviewAdapter adapter;

    @BindView(R.id.enotes)
    EditText eNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_create_review);
        createToolbar();
        ButterKnife.bind(this);

        adapter = new CalendarCreateReviewAdapter(this, calendarCreateReviewInfoList);
        listView.setAdapter(adapter);

        new ReadDataTask().execute();

        //

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
            for (AgencyInfo info : result) {
                agencyList.add(info);
            }


            for (CalendarDayCreate item : HAIRes.getInstance().calendarCreateSend.getItems()){
                CalendarCreateReviewInfo infoReview = new CalendarCreateReviewInfo(item.getDay(),1, item.getStatus(), item.getNotes());
                calendarCreateReviewInfoList.add(infoReview);
                if (item.getStatus().equals(HAIRes.getInstance().CALENDAR_CSKH)) {

                    for(String agency: item.getAgencies()) {
                        infoReview.setType(3);
                        AgencyInfo agencyInfo = findAgency(agency);
                        infoReview.setDeputy(agencyInfo.getDeputy() + " - " + agencyInfo.getCode());
                        infoReview.setName(agencyInfo.getName());
                        calendarCreateReviewInfoList.add(infoReview);
                    }

                } else {
                    infoReview.setType(2);
                    calendarCreateReviewInfoList.add(infoReview);
                }


                adapter.notifyDataSetChanged();
            }

            hidepDialog();
        }
    }

    private AgencyInfo findAgency(String code) {
        for(AgencyInfo info: agencyList) {
            if(code.equals(info.getCode()))
                return  info;
        }

        return  null;
    }
}
