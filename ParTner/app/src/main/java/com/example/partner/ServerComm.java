package com.example.partner;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ServerComm {

    private String TAG = "TAG";

    // 현재 쓰고있는 wifi ip (핸드폰이랑 노트북 쓰는 와이파이 같아야함!!)
    private String URL = "http://192.168.50.96:3000/";
    private RetrofitCommnunication retrofitCommnunication;

    public void init() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(createOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        retrofitCommnunication = retrofit.create(RetrofitCommnunication.class);

    }

    boolean check = false;
    public boolean idOverlapCheck(String type, String id, Context context){
        
        retrofitCommnunication.getOverlapCheck(type, id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "onResponse: response" + response.message());
                Toast.makeText(context,"중복체크가 완료되었습니다",Toast.LENGTH_LONG).show();
                check = true;
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onFailure: overlap 실패");
            }
        });
        return check;
    }

    public void postUploadImg(File profileImg, String trainerId, Context context) {

        Log.d(TAG, "postUploadImg: profileIMG > " + profileImg.getAbsolutePath());
        Log.d(TAG, "postUploadImg: image name : >> " + profileImg.getName());

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), profileImg);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", profileImg.getName(), requestFile);
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), trainerId);

        try {
            Log.d(TAG, "postUploadImg: desciption :>> " + description.contentLength());
        } catch (IOException e) {
            e.printStackTrace();
        }

        retrofitCommnunication.uploadFile(body, description).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "onResponse: 성공!!! response :>> " + response.message());
//                if(response.message().equals("success")){
                    Toast.makeText(context, "이미지를 업로드 하였습니다.", Toast.LENGTH_LONG).show();


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

    private static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        return builder.build();
    }

}
