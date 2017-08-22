package com.congtyhai.util;

/**
 * Created by HAI on 8/14/2017.
 */

public class HAIRes {

    public final String KEY_USER = "userlogin";
    public final String KEY_TOKEN = "tokenlogin";
    public final String KEY_INTENT_USER = "username";

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

    // save file path
    public final String PATH_AGENCY_JSON = "/agency.json";
    public final String PATH_RECEIVE_JSON = "/receive.json";
    public final String PATH_PRODUCT_JSON = "/product.json";

    // pref

    public final String PREF_KEY_USER = "HAIUSER";
    public final String PREF_KEY_TOKEN = "HAITOKEN";

    public final String PREF_KEY_UPDATE_DAILY = "updatedatadaily";
    public final String PREF_KEY_FIREBASE = "regFBId";
    public final String PREF_KEY_FUNCTION = "functionlogin";


}
