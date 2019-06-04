package com.example.partner;

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

    @GET("/trainers/list/{userId}/{trainerName}")
    Call<JsonObject> trainerList( @Path("userId") String id, @Path("trainerName") String name, @Query("traintype") String traintype, @Query("sex") String sex);

    @GET("/history/training/{id}/{year}/{month}")
    Call<List<TrainingHistory>> getTrainingHist( @Path("id") String id, @Path("year") String year, @Path("month") String month);

    @POST("/history/trainhistory/create")
    Single<JsonObject> postTrainingHist(@Body JsonObject trainingData);

    @POST("/history/callhistory/create")
    Call<JsonObject> postCallHistory(@Body JsonObject callData);
    
    @GET("/trainers/profile")
    Call<TrainerProfile> trainerProfile(@Query("id") String id);

    @POST("/trainers/profile/edit")
    Call<JsonObject> trainerEditProfile(@Body TrainerEditData trainerEditData);

    @POST("/trainers/starrate")
    Call<JsonObject> trainerStarRate(@Body JsonObject trainerStarRateData);

    @POST("/trainers/qb/id")
    Call<JsonObject> signupQB(@Body JsonObject qbid);

    @POST("/trainers/offline")
    Call<JsonObject> trainerOffline(@Body JsonObject trainer_id);

    @POST("/trainers/online")
    Call<JsonObject> trainerOnline(@Body JsonObject trainer_id);

    @GET("/trainers/state/{id}")
    Call<JsonObject> getTrainerState(@Path("id") String id);

    @POST("/sportsmans/bookmark/update")
    Call<JsonObject> bookmarkUpdate(@Body JsonObject trainerId);

    @POST("/sportsmans/bookmark/delete")
    Call<JsonObject> bookmarkDelete(@Body JsonObject trainerId);

    @GET("/sportsmans/bookmark/{id}")
    Call<List<TrainerProfile>> getBookmark(@Path("id") String id);

}
