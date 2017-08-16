package com.congtyhai.util;

import com.congtyhai.model.api.ResultInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by HAI on 8/10/2017.
 */

public interface ApiInterface {

    @GET("rest/loginsession")
    Call<ResultInfo> checkSession(
            @Query("user") String user,
            @Query("token") String token);
}
