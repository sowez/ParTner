package com.quickblox.sample.groupchatwebrtc.ParTner;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Observable;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

//retrofit
public interface RetrofitCommnunication {
    @GET("/users/signup/overlap/{type}/{id}")
    Single<JsonObject> getOverlapCheck(@Path("type") String type, @Path("id") String id);

    @POST("/users/signup")
    Call<SignUpData> postData(@Body SignUpData param);

    @Multipart
    @POST("/users/upload/image")
    Call<JsonObject> uploadFile(@Part MultipartBody.Part image, @Part("trainerId") RequestBody name);

    @POST("/users/login")
    Single<JsonObject> login(@Body JsonObject logindata);

    @GET("/trainers/list/{name}")
    Call<List<TrainerProfile>> trainerList( @Path("name") String name, @Query("traintype") String traintype, @Query("sex") String sex);

    @GET("/trainers/profile")
    Call<TrainerProfile> trainerProfile(@Query("id") String id);

}
