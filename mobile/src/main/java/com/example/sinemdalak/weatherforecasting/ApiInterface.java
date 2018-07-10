package com.example.sinemdalak.weatherforecasting;

import com.example.sinemdalak.weatherforecasting.model.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("forecast")
    Call<Example> getExampleResponse(@Query("id") String id, @Query("appid") String appid);

}
