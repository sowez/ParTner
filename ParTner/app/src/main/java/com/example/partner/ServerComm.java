package com.example.partner;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ServerComm {

    private String TAG ="TAG";
    private String URL = "http://192.168.43.53:8000/";
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

    public boolean postSignUp(SignUpData signUpData, Context context) {
        boolean result = false;
//server
        retrofitCommnunication.postData(signUpData).enqueue(new Callback<SignUpData>() {
            @Override
            public void onResponse(Call<SignUpData> call, Response<SignUpData> response) {
                if(response.isSuccessful()){
                    SignUpData body = response.body();
                    if(body!=null){
                        Log.d(TAG, "onResponse: --------------------------------------------");
                        Log.d(TAG, "onResponse: ID >>" + body.getId());
                        Log.d(TAG, "onResponse: ID >>" + body.getPw());
                        Log.d(TAG, "onResponse: ID >>" + body.getSex());
                        Log.d(TAG, "onResponse: ID >>" + body.getName());
                        Log.d(TAG, "onResponse: --------------------------------------------");
                    }

                    if(body.getId().equals("saved")){
                        Toast.makeText(context, "회원가입이 완료되었습니다!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);

                    }else if(body.getId().equals("exist")){
                        Toast.makeText(context, "이미 존재하는 아이디 입니다", Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<SignUpData> call, Throwable t) {
                Log.e(TAG, "onFailure: error" + t.getMessage());
            }
        });
        return result;
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
