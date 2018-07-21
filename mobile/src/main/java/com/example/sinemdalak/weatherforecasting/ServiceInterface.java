package com.example.sinemdalak.weatherforecasting;

import com.example.sinemdalak.weatherforecasting.model.AutoCompletePojo;
import com.example.sinemdalak.weatherforecasting.model.List;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceInterface {

    @GET("getcity/{name}")
    Call<ArrayList<AutoCompletePojo>> getServiceResponse(@Path("name") String name);
}
