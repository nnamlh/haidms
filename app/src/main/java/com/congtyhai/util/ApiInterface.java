package com.congtyhai.util;

import com.congtyhai.model.api.AgencyC1Info;
import com.congtyhai.model.api.AgencyCreateSend;
import com.congtyhai.model.api.AgencyInfo;
import com.congtyhai.model.api.AgencyModifySend;
import com.congtyhai.model.api.AuthInfo;
import com.congtyhai.model.api.CalendarCheckResult;
import com.congtyhai.model.api.CalendarCreateSend;
import com.congtyhai.model.api.CalendarShowResult;
import com.congtyhai.model.api.CalendarShowSend;
import com.congtyhai.model.api.CalendarUpdateSend;
import com.congtyhai.model.api.CheckCalendarUpdateResult;
import com.congtyhai.model.api.CheckInGetPlanResult;
import com.congtyhai.model.api.CheckInGetPlanSend;
import com.congtyhai.model.api.CheckInSend;
import com.congtyhai.model.api.CheckInTaskResult;
import com.congtyhai.model.api.CheckInTaskSend;
import com.congtyhai.model.api.CheckLocationRequest;
import com.congtyhai.model.api.CheckStaffResult;
import com.congtyhai.model.api.CodeSendInfo;
import com.congtyhai.model.api.CodeSendResult;
import com.congtyhai.model.api.DecorFolder;
import com.congtyhai.model.api.DecorImage;
import com.congtyhai.model.api.DecorImageSend;
import com.congtyhai.model.api.EventInfoSend;
import com.congtyhai.model.api.MainInfoResult;
import com.congtyhai.model.api.MainInfoSend;
import com.congtyhai.model.api.NotificationInfoResult;
import com.congtyhai.model.api.ProductCodeInfo;
import com.congtyhai.model.api.ProductDetailResult;
import com.congtyhai.model.api.RequestTracking;
import com.congtyhai.model.api.ResultEvent;
import com.congtyhai.model.api.ResultEventDetail;
import com.congtyhai.model.api.ResultInfo;
import com.congtyhai.model.api.ResultUpdate;
import com.congtyhai.model.api.StaffHelpRequest;
import com.congtyhai.model.api.TrackingResukt;
import com.congtyhai.model.api.UpdateProductInfo;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by HAI on 8/10/2017.
 */

public interface ApiInterface {

    // check session
    @GET("user/loginsession")
    Call<ResultInfo> checkSession(
            @Query("user") String user,
            @Query("token") String token, @Query("version") String version);

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

    // agency
    @GET("agency/getagencyc2")
    Call<AgencyInfo[]> getAgencyC2(
            @Query("user") String user,
            @Query("token") String token);

    @POST("agency/createagencyc2")
    Call<ResultInfo> createAgencyC2(@Body AgencyCreateSend info);

    @POST("agency/modifyagencyc2")
    Call<ResultInfo> modifyAgencyC2(@Body AgencyModifySend info);

    // c1
    @GET("agency/getagencyc1")
    Call<AgencyC1Info[]> getAgencyC1(
            @Query("user") String user,
            @Query("token") String token);

    @GET("showinfo/getproduct")
    Call<ProductCodeInfo[]> getProduct(
            @Query("user") String user,
            @Query("token") String token);

    @POST("checkin/checkin")
    Call<ResultInfo> checkIn(@Body CheckInSend info);

    @POST("checkin/checkintask")
    Call<CheckInTaskResult> checkInTask(@Body CheckInTaskSend info);

    @GET("showinfo/getproductdetail")
    Call<ProductDetailResult> getProductDetail(
            @Query("user") String user,
            @Query("token") String token,
            @Query("id") String id);

    // decor
    @GET("decor/getdecorfolder")
    Call<List<DecorFolder>> getDecorFolder(
            @Query("user") String user,
            @Query("token") String token);

    @POST("decor/getdecorimages")
    Call<List<DecorImage>> getDecorImages(@Body DecorImageSend info);

    @Multipart
    @POST("upload/decor")
    Call<ResultInfo> uploadImage(@Part MultipartBody.Part file, @Part("user") RequestBody user, @Part("token") RequestBody token, @Part("extension") RequestBody extension, @Part("agency") RequestBody agency,@Part("group") RequestBody group, @Part("lat") RequestBody lat, @Part("lng") RequestBody lng);

    //product
    @GET("product/getproducttask")
    Call<List<String>> getProductTask(
            @Query("user") String user,
            @Query("token") String token);


    // notification
    @GET("notification/get")
    Call<NotificationInfoResult> getNotifications(
            @Query("user") String user,
            @Query("token") String token, @Query("page") int page);


    @GET("notification/read")
    Call<ResultInfo> readNotification(
            @Query("user") String user,
            @Query("notification") String notification);


    @GET("checkin/calendarcheckupdate")
    Call<CheckCalendarUpdateResult> checkCalendarUpdate(
            @Query("user") String user,
            @Query("token") String token,
            @Query("day") int day, @Query("month") int month, @Query("year") int year);


    @POST("checkin/updatecalendar")
    Call<ResultInfo> updateCalendar(@Body CalendarUpdateSend info);

    @GET("showinfo/checkstaff")
    Call<CheckStaffResult> checkStaff(
            @Query("user") String user,
            @Query("token") String token,
            @Query("code") String code);

    //

    @POST("product/updateproduct")
    Call<ResultUpdate> updateProduct(@Body UpdateProductInfo info);

    @POST("product/checklocationdistance")
    Call<ResultInfo> checkLocationDistance(@Body CheckLocationRequest info);

    @POST("product/helpagencyimport")
    Call<ResultUpdate> updateAgencyimport(@Body StaffHelpRequest info);


    @POST("product/tracking")
    Call<TrackingResukt> tracking(@Body RequestTracking info);

    //
    @POST("event/sendcodeevent")
    Call<CodeSendResult> sendCode(@Body CodeSendInfo info);

    @POST("event/loyaltyevent")
    Call<ResultEvent> loyaltyEvent(@Body AuthInfo auth);


    @POST("event/eventdetail")
    Call<ResultEventDetail> eventDetail(@Body EventInfoSend info);
}
