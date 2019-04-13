package com.example.partner;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

//retrofit
public interface RetrofitCommnunication {

    @POST("/users/signup")
    Call<SignUpData> postData(@Body SignUpData param);

    @Multipart
    @POST("/users/upload/image")
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part image, @Part("upload") RequestBody name);

//    Call<ResponseBody> uploadFile(@Part("description") RequestBody description,
//                                  @Part MultipartBody.Part file);
}
