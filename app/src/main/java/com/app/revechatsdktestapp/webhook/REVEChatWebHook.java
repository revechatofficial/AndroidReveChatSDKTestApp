package com.app.revechatsdktestapp.webhook;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;
import java.io.IOException;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public enum REVEChatWebHook {
    INSTANCE;

    private static final String TAG = "REVEChatWebHook";

    // below things will be changed based on account
    // staging environment
//    private static final String WEB_HOOK_BASE_URL = "https://chat-socket-staging-commercial.revechat.com/";
//    private static final String AUTHORIZATION_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiY2hhdGJvdCIsImlhdCI6MTc0NDc4MjU3MywidXNlcm5hbWUiOiJlYzlkNTM2Ny04MTgxLTQxNjMtYTQ3Zi02MjEyZTFkOTM4YWYiLCJ0aW1lc3RhbXAiOjE3NDQ3ODI1NzM5OTN9.1LCNT7ZpoKzrGEmBT-2c4DRqX3W1kvCiWKOXyURdIEY";

    // production environment
    private static final String WEB_HOOK_BASE_URL = "https://chat02.revechat.com/";
    private static final String AUTHORIZATION_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiY2hhdGJvdCIsImlhdCI6MTc0NDI4NDUyMiwidXNlcm5hbWUiOiJkOGU0MjJmNC03YTZmLTRmMTMtODVkYS1kYTFlY2UzNDk1NGEiLCJ0aW1lc3RhbXAiOjE3NDQyODQ1MjI0MTd9.QPhLNt4LEQrFHzTw4ioL-_zM_Y5kIjJStOrV8qzQR8Q";


    public static final String REVECHAT_WEB_HOOK_BOT_ID = "revechat_web_hook_bot_id";
    public static final String REVECHAT_WEB_HOOK_SESSION_ID = "revechat_web_hook_session_id";


    private JSONObject dataObject;
    private boolean isLoggedIn = false;


    public void setData(JSONObject jsonObject) {
        Log.i(TAG, "setData jsonObject = "+jsonObject.toString());
        this.dataObject = jsonObject;
        Log.i(TAG, "setData jsonObject = "+dataObject);
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public void loginToChatbot(String botId, String sessionId) {
        Log.i(TAG, "loginToChatbot: botId = "+botId+ " sessionId = "+sessionId);
        try {
            JSONObject root = new JSONObject();
            root.put("botId", botId);
            root.put("sessionId", sessionId);
            root.put("data", dataObject);

            // Create RequestBody
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(root.toString(), JSON);

            // Retrofit instance
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(WEB_HOOK_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient())
                    .build();

            ChatbotApi chatbotApi = retrofit.create(ChatbotApi.class);

            Call<ResponseBody> call;
            if (isLoggedIn) {
                call = chatbotApi.loginEndpoint(
                        "Bearer "+AUTHORIZATION_TOKEN,
                        requestBody
                );
            } else {
                call = chatbotApi.logoutEndpoint(
                        "Bearer "+AUTHORIZATION_TOKEN,
                        requestBody
                );
            }

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            Log.i(TAG, "Success: " + response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e(TAG, "Failed: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "Network error", t);
                }
            });


        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


}
