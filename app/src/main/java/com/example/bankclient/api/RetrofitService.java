package com.example.bankclient.api;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;
    private final String url = "http://127.0.0.1:5049";
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
