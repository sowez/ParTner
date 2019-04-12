package com.example.partner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
//retrofit
public interface RetrofitCommnunication {

    @POST("/users/signup")
    Call<SignUpData> postData(@Body SignUpData param);
}
