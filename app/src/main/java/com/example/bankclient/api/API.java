package com.example.bankclient.api;

import com.example.bankclient.ui.models.Plan;
import com.example.bankclient.ui.models.PostData;
import com.example.bankclient.ui.models.Result;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface API {
    @GET("/plan/{id}")
    Call<Result> getResultById();

    @POST("/api/matrix")
    Call<String> addMatrix(@Body PostData postData);
}
