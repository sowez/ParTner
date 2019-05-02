package com.example.partner;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface RetrofitCommnunication {

    @GET("/users/signup/overlap/{type}/{id}")
    Call<JsonObject> getOverlapCheck(@Path("type") String type, @Path("id") String id);

    @POST("/users/signup")
    Call<SignUpData> postData(@Body SignUpData param);

    @Multipart
    @POST("/users/upload/image")
    Call<JsonObject> uploadFile(@Part MultipartBody.Part image, @Part("trainerId") RequestBody name);

}
