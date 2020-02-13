package com.car.toll_car.Model.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClint {

    public static Retrofit getRetrifitClint(){
        return new Retrofit.Builder()
                .baseUrl("http://192.168.50.10/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
