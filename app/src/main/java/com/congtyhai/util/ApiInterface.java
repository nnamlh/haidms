package com.congtyhai.util;

import com.congtyhai.model.api.AgencyCreateSend;
import com.congtyhai.model.api.AgencyInfo;
import com.congtyhai.model.api.AgencyModifySend;
import com.congtyhai.model.api.CalendarCheckResult;
import com.congtyhai.model.api.CalendarCreateSend;
import com.congtyhai.model.api.CalendarShowResult;
import com.congtyhai.model.api.CalendarShowSend;
import com.congtyhai.model.api.CheckInGetPlanResult;
import com.congtyhai.model.api.CheckInGetPlanSend;
import com.congtyhai.model.api.CheckInResult;
import com.congtyhai.model.api.CheckInSend;
import com.congtyhai.model.api.CheckInTaskResult;
import com.congtyhai.model.api.CheckInTaskSend;
import com.congtyhai.model.api.MainInfoResult;
import com.congtyhai.model.api.MainInfoSend;
import com.congtyhai.model.api.ResultInfo;
import com.congtyhai.model.api.SendBasicInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by HAI on 8/10/2017.
 */

public interface ApiInterface {

    // check session
    @GET("user/loginsession")
    Call<ResultInfo> checkSession(
            @Query("user") String user,
            @Query("token") String token);

    // main info
    @POST("restmain/maininfo")
    Call<MainInfoResult> updateReg(@Body MainInfoSend info);

    // calendar
    @GET("checkin/calendarcheck")
    Call<CalendarCheckResult> calendarCheck(
            @Query("user") String user);

    @POST("checkin/calendarcreate")
    Call<ResultInfo> calendarCreate(@Body CalendarCreateSend info);

    @POST("checkin/calendarshow")
    Call<CalendarShowResult> calendarShow(@Body CalendarShowSend info);


    @POST("checkin/checkingetplan")
    Call<CheckInGetPlanResult> checkInGetPlan(@Body CheckInGetPlanSend info);

    @POST("restv2/GetStaffAgencyC2")
    Call<AgencyInfo[]> getAgencyC2(@Body SendBasicInfo info);

    @POST("restv2/CreateAgencyC2")
    Call<ResultInfo> createAgencyC2(@Body AgencyCreateSend info);

    @POST("restv2/modifyagencyc2")
    Call<ResultInfo> modifyAgencyC2(@Body AgencyModifySend info);

    @POST("restv2/checkin")
    Call<CheckInResult> checkIn(@Body CheckInSend info);

    @POST("checkin/checkintask")
    Call<CheckInTaskResult> checkInTask(@Body CheckInTaskSend info);
}
