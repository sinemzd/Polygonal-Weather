package com.example.sinemdalak.weatherforecasting;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String url = "https://api.openweathermap.org/data/2.5/";
    private static Retrofit retrofit = null;
    public static Retrofit getClient(){
        if(retrofit == null){

            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit;

        }return retrofit;
    }

}
