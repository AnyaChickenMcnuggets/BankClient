package com.example.bankclient.retrofit;

import com.example.bankclient.model.Matrix;
import com.example.bankclient.model.Plan;
import com.example.bankclient.model.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface API {
    @GET("/plan/{id}")
    Call<Result> getResultById();

    @POST("/plan/count")
    Call<Matrix> addMatrix(@Body Plan plan);
}
