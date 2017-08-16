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


    public final String PREF_KEY_USER = "HAIUSER";
    public final String PREF_KEY_TOKEN = "HAITOKEN";

    private static HAIRes instance = null;

    public static HAIRes getInstance() {
        if (instance == null) {
            instance = new HAIRes();
        }
        return instance;
    }
}
