package com.congtyhai.util;

import com.congtyhai.model.api.CalendarCreateSend;
import com.congtyhai.model.api.CheckCalendarResult;
import com.congtyhai.model.api.MainInfoResult;
import com.congtyhai.model.api.MainInfoSend;
import com.congtyhai.model.api.ResultInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by HAI on 8/10/2017.
 */

public interface ApiInterface {

    @GET("rest/loginsession")
    Call<ResultInfo> checkSession(
            @Query("user") String user,
            @Query("token") String token);

    @POST("rest/getmaininfo")
    Call<MainInfoResult> updateReg(@Body MainInfoSend info);

    @GET("restv2/CheckCalendarCreate")
    Call<CheckCalendarResult> checkCalendarCreate(
            @Query("user") String user);

    @POST("restv2/CheckInCalendarCreate")
    Call<ResultInfo> calendarCreate(@Body CalendarCreateSend info);


}
