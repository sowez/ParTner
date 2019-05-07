package com.example.partner;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

//retrofit
public interface RetrofitCommnunication {

    @POST("/users/signup")
    Call<SignUpData> postData(@Body SignUpData param);

    @GET("/trainers/list/{name}")
    Call<List<TrainerProfile>> trainerList( @Path("name") String name, @Query("traintype") String traintype, @Query("sex") String sex);

}
