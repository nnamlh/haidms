package com.congtyhai.di.module;

import com.congtyhai.di.scope.RetrofitUploadInfo;
import com.congtyhai.util.ApiInterface;
import com.congtyhai.util.HAIRes;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HAI on 8/10/2017.
 */

@Module
public class NetModule {

    //String mBaseUrl = "http://api.nongduochai.vn/api/";
   // String mBaseUrlUpload = "http://cskh.nongduochai.vn/";

    public NetModule() {

    }

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(HAIRes.getInstance().baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    @Provides
    @Singleton
    @RetrofitUploadInfo
    Retrofit provideRetrofitUpload() {
        return new Retrofit.Builder()
                .baseUrl(HAIRes.getInstance().baseUrlUpload)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


}
