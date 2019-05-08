package com.example.partner;

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
    private Disposable disposable;

    /* 현재 쓰고있는 wifi ip (핸드폰이랑 노트북 쓰는 와이파이 같아야함!!) */
    /* 성희가 쓰는 ip 입니당
    * 집 : http://192.168.0.10:8000/
    * 프실:
    * 세미나실 : http://192.168.50.96:8000/
    * 종합관 509 :  http://192.168.30.96:8000/
    * */
    private String URL = "http://192.168.0.10:8000/";

    // 수진이 핫스팟 : private String URL = "http://192.168.43.53:8000/";

    private RetrofitCommnunication retrofitCommnunication;

    public RetrofitCommnunication init() {

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
                        context.startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<SignUpData> call, Throwable t) {
                Log.e(TAG, "onFailure: error" + t.getMessage());
            }
        });
    }

    // searching trainer list
    List<TrainerProfile> trainerProfileList;
    public List<TrainerProfile> searchTrainer(String name, String traintype, String sex, Context context) {

        retrofitCommnunication.trainerList(name, traintype, sex).enqueue(new Callback<List<TrainerProfile>>() {
            @Override
            public void onResponse(Call<List<TrainerProfile>> call, Response<List<TrainerProfile>> response) {
                if(response.isSuccessful()){
                    trainerProfileList = response.body();
                    Log.i(TAG, response.body().get(0).getId());
                    Log.i(TAG, trainerProfileList.get(0).getId());
                }
            }

            @Override
            public void onFailure(Call<List<TrainerProfile>> call, Throwable t) {
                Toast.makeText(context, "데이터를 불러오는 데 실패하였습니다.", Toast.LENGTH_LONG).show();
                Log.e(TAG, "onFailure: error getting trainerprofile" + t.getMessage());
            }
        });

        return trainerProfileList;
    }

    private static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        return builder.build();
    }

}
