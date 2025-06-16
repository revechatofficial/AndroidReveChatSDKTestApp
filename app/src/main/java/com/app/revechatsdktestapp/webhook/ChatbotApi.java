package com.app.revechatsdktestapp.webhook;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChatbotApi {

//    @POST("api/webhook/v1/chatbot/ec9d5367-8181-4163-a47f-6212e1d938af/login")  // staging
    @POST("api/webhook/v1/chatbot/d8e422f4-7a6f-4f13-85da-da1ece34954a/login")  // production
    Call<ResponseBody> loginEndpoint(
            @Header("Authorization") String token,
            @Body RequestBody requestBody
    );



//    @POST("api/webhook/v1/chatbot/ec9d5367-8181-4163-a47f-6212e1d938af/logout")  // staging
    @POST("api/webhook/v1/chatbot/d8e422f4-7a6f-4f13-85da-da1ece34954a/logout")  // staging
    Call<ResponseBody> logoutEndpoint(
            @Header("Authorization") String token,
            @Body RequestBody requestBody
    );



}
