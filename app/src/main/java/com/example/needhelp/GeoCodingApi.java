package com.example.needhelp;

import com.example.needhelp.geocodingModels.Results;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeoCodingApi {

    @GET("json?")
    Call<Results> getLocation(@Query("address") String fromAdress, @Query("key") String key);
}
