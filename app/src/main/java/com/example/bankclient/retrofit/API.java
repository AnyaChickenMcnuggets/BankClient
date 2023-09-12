package com.example.bankclient.retrofit;

import com.example.bankclient.model.Plan;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface API {
    @GET("/plan/count")
    Call<Plan> getPlan();

    @POST("/plan/count")
    Call<Plan> sendPlan(@Body Plan plan);
}
