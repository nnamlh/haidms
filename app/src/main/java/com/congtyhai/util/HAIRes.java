package com.congtyhai.util;

import com.congtyhai.model.api.AgencyInfo;
import com.congtyhai.model.api.CalendarCreateSend;
import com.congtyhai.model.api.CalendarStatus;
import com.congtyhai.model.api.ProductCodeInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by HAI on 8/14/2017.
 */

public class HAIRes {

    // broadcast receiver intent filters
    public final String REGISTRATION_COMPLETE = "registrationComplete";
    public final String PUSH_NOTIFICATION = "pushNotification";
    // global topic to receive app wide push notifications
    public final String TOPIC_GLOBAL = "global";
    // id to handle the notification in the notification tray
    public final int NOTIFICATION_ID = 100;
    public final String SHARED_PREF = "ah_firebase";

    public final String KEY_USER = "userlogin";
    public final String KEY_TOKEN = "tokenlogin";
    public final String KEY_INTENT_USER = "username";
    public final String KEY_INTENT_CREATE_CALENDAR = "createcalendar";
    public final String KEY_INTENT_AGENCY_CODE = "agencycode";
    public final String KEY_INTENT_TEMP = "temp";
    public final String KEY_INTENT_TEMP2 = "temp2";

  //  public final String baseUrl = "http://192.168.2.170:802/api/";
   // public final String baseUrlUpload = "http://192.168.2.170:801/";
  public final String baseUrl = "http://dmsapi.nongduochai.vn/api/";
    public final String baseUrlUpload = "http://dms.nongduochai.vn/";

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
    public final String PATH_PRODUCT_GROUP_JSON = "/productgroup.json";
    public final String PATH_AGENCY_JSON = "/agency.json";
    public final String PATH_AGENCY_C1_JSON = "/agencyc1.json";
    public final String PATH_RECEIVE_JSON = "/receive.json";
    public final String PATH_PRODUCT_JSON = "/product.json";

    // pref

    public final String PREF_KEY_USER = "HAIUSER";
    public final String PREF_KEY_TOKEN = "HAITOKEN";
    public final String PREF_KEY_TYPE= "HAITYPE";

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


    // product
    public String PRODUCT_IMPORT = "NK";
    public String PRODUCT_EXPORT = "XK";
    public String PRODUCT_TRANSPORT= "TRANSPORT";
    public String PRODUCT_HELP_SCAN = "HELPSCAN";
    public List<String> LIST_PRODUCT;
    public void resetListProduct() {
        LIST_PRODUCT = new ArrayList<>();
    }

    private HashMap<String, String> productCodeMap = new HashMap<>();
    public void clearProductCodeMap() {
        productCodeMap.clear();
    }

    public void addProductCodeMap(ProductCodeInfo info) {
        productCodeMap.put(info.getCode(), info.getName());
    }
    public void addListProduct(String item) {
        if (LIST_PRODUCT == null) {
            LIST_PRODUCT = new ArrayList<>();
        }

        LIST_PRODUCT.add(0, item);
    }

    public int countListProduct() {
        return getLIST_PRODUCT().size();
    }

    public List<String> getLIST_PRODUCT() {
        if (LIST_PRODUCT == null) {
            LIST_PRODUCT = new ArrayList<>();
        }

        return LIST_PRODUCT;
    }

    public String[] toProductArrays() {
        String[] arrays = new String[HAIRes.getInstance().countListProduct()];

        int i = 0;
        for (String item : LIST_PRODUCT) {
            arrays[i] = item;
            i++;
        }

        return arrays;
    }
    public String  findProductNameByCode(String code) {

        if (productCodeMap.containsKey(code)) {
            return productCodeMap.get(code);
        }

        return "KHÔNG PHẢI SẢN PHẨM CỦA HAI";
    }


}
