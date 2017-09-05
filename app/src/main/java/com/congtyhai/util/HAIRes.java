package com.congtyhai.util;

import com.congtyhai.model.api.AgencyInfo;
import com.congtyhai.model.api.CalendarCreateSend;
import com.congtyhai.model.api.CalendarStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HAI on 8/14/2017.
 */

public class HAIRes {

    public final String KEY_USER = "userlogin";
    public final String KEY_TOKEN = "tokenlogin";
    public final String KEY_INTENT_USER = "username";
    public final String KEY_INTENT_CREATE_CALENDAR = "createcalendar";
    public final String KEY_INTENT_AGENCY_CODE = "agencycode";
    public final String KEY_INTENT_TEMP = "temp";

    public final String baseUrl = "http://192.168.2.170:802/api/";
    public final String baseUrlUpload = "http://192.168.2.170:801/";


    private static HAIRes instance = null;

    public static HAIRes getInstance() {
        if (instance == null) {
            instance = new HAIRes();
        }
        return instance;
    }

    // code menu check in
    public final int CHECKIN_CHECK = 1;
    public final int CHECKIN_PICTURE = 2;
    public final int CHECKIN_ORDER = 3;
    public final int CHECKIN_PRODUCT = 4;

    // code menu bottom calendar
    public final int CALENDAR_CREATE = 1;
    public final int CALENDAR_MODIFY = 2;

    // save file path
    public final String PATH_AGENCY_JSON = "/agency.json";
    public final String PATH_AGENCY_C1_JSON = "/agencyc1.json";
    public final String PATH_RECEIVE_JSON = "/receive.json";
    public final String PATH_PRODUCT_JSON = "/product.json";

    // pref

    public final String PREF_KEY_USER = "HAIUSER";
    public final String PREF_KEY_TOKEN = "HAITOKEN";

    public final String PREF_KEY_UPDATE_DAILY = "updatedatadaily";
    public final String PREF_KEY_FIREBASE = "regFBId";
    public final String PREF_KEY_FUNCTION = "functionlogin";

    // data
    public List<CalendarStatus> statusInfos = new ArrayList<>();
    public CalendarCreateSend calendarCreateSend = new CalendarCreateSend();
    public AgencyInfo currentAgencySelect = new AgencyInfo();
    public final double LIMIT_DISTANCE = 300;

    public String[] GetListStatusName() {
        String[] statusTemp = new String[statusInfos.size()];
        for(int i=0; i< statusInfos.size(); i++) {
            statusTemp[i] = statusInfos.get(i).name;
        }

        return statusTemp;
    }

    public int findPostitionStatus(String status) {
        for(int i=0; i< statusInfos.size(); i++) {
            if (status.equals(statusInfos.get(i).id)){
                return  i;
            }
        }

        return 0;
    }

    public String findStatusName(String status) {
        for(int i=0; i< statusInfos.size(); i++) {
            if (status.equals(statusInfos.get(i).id)){
                return statusInfos.get(i).name;
            }
        }

        return "";
    }

    // status
    public final String CALENDAR_CSKH = "CSKH";
    public final String CALENDAR_OTHER = "OTHER";
}
