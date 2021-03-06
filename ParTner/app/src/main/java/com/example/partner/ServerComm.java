package com.example.partner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.transition.Scene;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ServerComm {

    private String TAG = "TAG";

    private static String URL = "http://ec2-34-203-233-238.compute-1.amazonaws.com:8000/";


    public String getURL() {
        return URL;
    }

    private static RetrofitCommnunication retrofitCommnunication;

    public static RetrofitCommnunication init() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(createOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        retrofitCommnunication = retrofit.create(RetrofitCommnunication.class);
        return retrofitCommnunication;
    }

    public void userLogout(String id){
        JsonObject logoutdata = new JsonObject();
        logoutdata.addProperty("id", id);
        retrofitCommnunication.logout(logoutdata).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject res = response.body();
                if(res.get("result").equals("logout_success")){
                    Log.d("logout", "servercomm logout onResponse: 로그아웃성공");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void postUploadImg(File profileImg, String trainerId, Context context) {

        Log.d(TAG, "postUploadImg: profileIMG > " + profileImg.getAbsolutePath());
        Log.d(TAG, "postUploadImg: image name : >> " + profileImg.getName());

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), profileImg);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", profileImg.getName(), requestFile);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), trainerId);

        retrofitCommnunication.uploadFile(body, description).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject res = response.body();
                if (res.get("result").getAsString().equals("success")) {
                    Log.d(TAG, "onResponse: 이미지 업로드 성공");
                }else{
                    Log.d(TAG, "onResponse: 이미지 업로드 실패");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onFailure: 실패");
            }
        });
    }

    public void postSignUp(SignUpData signUpData, Context context) {

        retrofitCommnunication.postData(signUpData).enqueue(new Callback<SignUpData>() {
            @Override
            public void onResponse(Call<SignUpData> call, Response<SignUpData> response) {
                if (response.isSuccessful()) {
                    SignUpData body = response.body();
                    if (body != null) {
                        Log.d(TAG, "onResponse: --------------------------------------------");
                        Log.d(TAG, "onResponse: ID >>" + body.getId());
                        Log.d(TAG, "onResponse: ID >>" + body.getPw());
                        Log.d(TAG, "onResponse: ID >>" + body.getSex());
                        Log.d(TAG, "onResponse: ID >>" + body.getName());
                        Log.d(TAG, "onResponse: --------------------------------------------");
                    }

                    if (body.getResult().equals("saved")) {
                        Toast.makeText(context, "회원가입이 완료되었습니다!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, LoginActivity.class);
//                        context.startActivity(intent);
                        ((Activity)context).finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<SignUpData> call, Throwable t) {
                Log.e(TAG, "onFailure: error" + t.getMessage());
            }
        });
    }

    public void setTrainerOffline(String trainer_id, Context context){
        JsonObject t_id = new JsonObject();
        t_id.addProperty("id", trainer_id);
        retrofitCommnunication.trainerOffline(t_id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject res = response.body();
                if(res.get("result").getAsString().equals("success")){
                    Toast.makeText(context, "offline성공", Toast.LENGTH_SHORT).show();
                }else{
                    Log.e(TAG, "onResponse: 실패");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "servercomm settraineroffline onFailure: 오프라인 등록실패!");
            }
        });
    }

    public void setTrainerOnline(String trainer_id, Context context){
        JsonObject t_id = new JsonObject();
        t_id.addProperty("id", trainer_id);
        retrofitCommnunication.trainerOnline(t_id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject res = response.body();
                if(res.get("result").getAsString().equals("success")){
                    Toast.makeText(context, "offline성공", Toast.LENGTH_SHORT).show();
                }else{
                    Log.e(TAG, "onResponse: 실패");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "servercomm settraineroffline onFailure: 오프라인 등록실패!");
            }
        });
    }

    public void updateSportsmanBookmarkList(String sportsman_id, String trainer_id, Context context){
        JsonObject bookmark = new JsonObject();
        bookmark.addProperty("sportsmanID", sportsman_id);
        bookmark.addProperty("trainerID", trainer_id);

        retrofitCommnunication.bookmarkUpdate(bookmark).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject res = response.body();
                if(res.get("result").getAsString().equals("success")){
                    Toast.makeText(context, "트레이너 즐겨찾기가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "트레이너 즐겨찾기가 실패하었습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("bookmark", "sportsman bookmark onFailure");
            }
        });
    }

    public void deleteSportsmanBookmarkList(String sportsman_id, String trainer_id, Context context){
        JsonObject bookmark = new JsonObject();
        bookmark.addProperty("sportsmanID", sportsman_id);
        bookmark.addProperty("trainerID", trainer_id);

        retrofitCommnunication.bookmarkDelete(bookmark).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject res = response.body();
                if(res.get("result").getAsString().equals("success")){
                    Toast.makeText(context, "트레이너 즐겨찾기가 해제되었습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "트레이너 즐겨찾기 해제가 실패하었습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("bookmark", "sportsman bookmark onFailure");
            }
        });
    }



    List<TrainingHistory> trainingHistoryList;
    public List<TrainingHistory> searchTrainingHist(String id, String year, String month, Context context){
        retrofitCommnunication.getTrainingHist(id, year, month).enqueue(new Callback<List<TrainingHistory>>() {
            @Override
            public void onResponse(Call<List<TrainingHistory>> call, Response<List<TrainingHistory>> response) {
                if(response.isSuccessful()){
                    trainingHistoryList = response.body();
                    Log.d(TAG, "onResponse: ");
                }
            }

            @Override
            public void onFailure(Call<List<TrainingHistory>> call, Throwable t) {
                Toast.makeText(context, "트레이닝 데이터를 불러오는 데 실패하였습니다.", Toast.LENGTH_LONG).show();
                Log.e(TAG, "onFailure: error getting trainingHistory" + t.getMessage());

            }
        });
        return trainingHistoryList;
    }

    private static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        return builder.build();
    }

}
