package com.congtyhai.util;

import com.congtyhai.model.api.CheckUserLoginResult;
import com.congtyhai.model.api.LoginResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by HAI on 8/15/2017.
 */

public interface LoginService {
    @GET("user/login")
    Call<LoginResult> basicLogin(@Query("imei") String imei);

    @GET("user/checkuserlogin")
    Call<CheckUserLoginResult> checkUserLogin();

    @GET("user/loginactivaton")
    Call<LoginResult> loginActivaton();

}