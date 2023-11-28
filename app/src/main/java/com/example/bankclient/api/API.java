package com.example.bankclient.api;

import com.example.bankclient.ui.models.Result;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API {
    @GET("/plan/{id}")
    Call<Result> getResultById();

//    @POST("/plan/count")
//    Call<Matrix> addMatrix(@Body Plan plan);
}
