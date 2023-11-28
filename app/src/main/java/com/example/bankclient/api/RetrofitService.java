package com.example.bankclient.api;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;
    private final String url = "http://192.168.43.1:8080";
    public RetrofitService() {
        InitializeRetrofit();
    }

    private void InitializeRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }
}
