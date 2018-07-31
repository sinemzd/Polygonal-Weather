package com.sinemdalak.weatherforecasting;

import com.sinemdalak.weatherforecasting.model.Example;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiInterface {

    @GET("forecast")
    Call<Example> getCityResponseById(@Query("id") String id, @Query("appid") String appid);

    @GET("forecast")
    Call<Example> getExampleResponse(@QueryMap HashMap<String,Object> map);

}
