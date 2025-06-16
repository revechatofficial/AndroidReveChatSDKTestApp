package com.app.revechatsdktestapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.app.revechatsdktestapp.webhook.REVEChatWebHook
import com.google.gson.Gson
import com.revesoft.revechatsdk.data.remote.getglobal.RetrofitClient.userService
import com.revesoft.revechatsdk.data.remote.getglobal.TokenVerifyResponse
import com.revesoft.revechatsdk.event.REVEChatEventListener
import com.revesoft.revechatsdk.event.REVEChatEventManager.registerListener
import com.revesoft.revechatsdk.model.VisitorInfo
import com.revesoft.revechatsdk.service.REVEChatApiService
import com.revesoft.revechatsdk.ui.activity.ReveChatActivity
import com.revesoft.revechatsdk.utils.ReveChat
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), REVEChatEventListener {

    private lateinit var accountIdEditText: EditText
    private lateinit var userNameEditText: EditText
    private lateinit var emailIdEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var loginStateCheckBox: CheckBox

    private val accountId = "2552651"  // USE YOUR ACCOUNT ID
    private val userName = "androidSDKLib"
    private val userEmail = "androidSDK@test.com"
    private val userPhone = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 35) {
            WindowCompat.setDecorFitsSystemWindows(window, true)
        }
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }

        accountIdEditText = findViewById(R.id.accountID)
        accountIdEditText.setText(accountId)
        accountIdEditText.isEnabled = false
        userNameEditText = findViewById(R.id.userName)
        userNameEditText.setText(userName)
        emailIdEditText = findViewById(R.id.userEmail)
        emailIdEditText.setText(userEmail)
        phoneNumberEditText = findViewById(R.id.userPhone)
        phoneNumberEditText.setText(userPhone)

        loginStateCheckBox = findViewById(R.id.login_state_checkbox)


        findViewById<Button>(R.id.startChat).setOnClickListener {
            openChatWindow()
        }

        // this need to be called
        initiateReveChat()
        initializeReveChatWebHook()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!REVEChatApiService.isCallRunning())
            stopService(Intent(this, REVEChatApiService::class.java))
    }


    private fun openChatWindow() {
        startActivity(Intent(this, ReveChatActivity::class.java))
    }



    private fun initiateReveChat() {
        // test access token value only
//        val accessToken =
//            "Basic eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHRlcm5hbF9zeXN0ZW1fZW1haWwiOiJ0ZXN0cUBnbWFpbC5jb20iLCJmaXJzdE5hbWUiOiIiLCJsYXN0TmFtZSI6IiIsImNvbXBhbnlfaWQiOjExMjksInVzZXJfbmFtZSI6InRlc3RxQGdtYWlsLmNvbSIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE3NDM4MjYyNDQsImF1dGhvcml0aWVzIjpbImNyZXciXSwianRpIjoiNjIwZmY2OWEtYjc3OS00ZjgzLThkMTEtOTA5MzBhYjc2OGMzIiwiY2xpZW50X2lkIjoid2ViIn0.QNGrQeEDUQyhAyqrg9SASVXxHxcbq62gNxR73-CsrTnn_lyufdoHr8-5UHQyrXO4ddUq9S3DNonp8VgrdiuH_oR2dwTZFy9mmwT7RbXcL33lke1hBitCIVLMXR8qyNPfBtRXTYgKYvbQVFu9hA3nlMBj4iFQb8VpAgYCo2isuFDdsTyV1T8Oevd40aecrP1PAImKVdNQVr9zJ9d-mhIrreo-4lxuhUC5cbHw1sC7xQkwk5rLWP8cMvxEBxizZVtuAoFsnjZTasMZXl6VMk61RR77fRpfbmWvmeG2FlQOiTBqlejh1Hv74ykuB9J0O18yTBKHxcwBuJUwACL81gWUOg"

        ReveChat.init(accountId)

        //create visitor info
        val visitorInfo: VisitorInfo = VisitorInfo.Builder()
            .name(userName) // set name
            .email(userEmail)
            .phoneNumber(userPhone)
            .build()

//      visitorInfo.accessToken = accessToken // set access token if any

        ReveChat.setVisitorInfo(visitorInfo)

        ReveChat.setAppBundleName(BuildConfig.APPLICATION_ID)
        ReveChat.setAppVersionNumber(BuildConfig.VERSION_NAME)
        startService(Intent(this, REVEChatApiService::class.java))
    }


//    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//        result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            Toast.makeText(this, "RESULT_OK", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(this, "Operation Canceled", Toast.LENGTH_SHORT).show()
//        }
//    }


    // WebHOOK integration
    private val TAG = "MainActivity"

    private val ACCESS_TOKEN: String =
        "Basic eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHRlcm5hbF9zeXN0ZW1fZW1haWwiOiJzaGl2NjBAeW9wbWFpbC5jb20iLCJmaXJzdE5hbWUiOiIiLCJsYXN0TmFtZSI6IiIsImNvbXBhbnlfaWQiOjExMjksInVzZXJfbmFtZSI6InNoaXY2MEB5b3BtYWlsLmNvbSIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE3NTI5OTExNTEsImF1dGhvcml0aWVzIjpbImNyZXciXSwianRpIjoiY2RmNjJjMTEtZWEyZC00MjJiLThmMDYtYmE3MGVhNmUyZTRhIiwiY2xpZW50X2lkIjoid2ViIn0.rwEvFuSGttr3WTw2Dd61gmH6V4G9MOQdwI6iHLg5swGazcw9_GvQITlHTpOXnpzgfljAfN9b_XB6FdelxnysO7vgqmbk7W-ZphCTUGktFWKAqIarEHKSBygFajI-zjGrGdXZqEjYiOM4y_KihjZLlp_1dtT4SBNv8Htzy9L44MfLxumpdB0OKR9FMUKnT1QElqcIZAL3ahQ6ZoMivMLfBYJAk584PobvKl-VBZ0aGYTFrnHglEzltf0O5AifER0ZYnNfOmLze8t2b51e9fK0I3zalytqKSszEvNu2Ox5SmO5Mq93OJU_lL-4qdE8LzmlJI2I9zboyHhqIZTRQwW6Vw"

    private var reveChatBotId: String? = null
    private var reveSessionId: String? = null


    var USER_PREFERENCES: String = "revechat_registration_preferences"

    private fun initializeReveChatWebHook() {
        Log.i(TAG, "initializeReveChatWebHook() ++")
        registerListener(this)

        // restore bot id and session id values
        val sharedPref = this.getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE)
        reveChatBotId = sharedPref.getString(REVEChatWebHook.REVECHAT_WEB_HOOK_BOT_ID, "")
        reveSessionId = sharedPref.getString(REVEChatWebHook.REVECHAT_WEB_HOOK_SESSION_ID, "")

        Log.i(TAG, "chatBotId = $reveChatBotId")
        Log.i(TAG, "sessionId = $reveSessionId")

        Log.i(TAG, "initializeReveChatWebHook() --")
    }


    private fun saveReveChatBotSessionInfo() {
        val sharedPref = this.getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE)
        sharedPref.edit()
            .putString(REVEChatWebHook.REVECHAT_WEB_HOOK_BOT_ID, reveChatBotId)
            .putString(REVEChatWebHook.REVECHAT_WEB_HOOK_SESSION_ID, reveSessionId)
            .apply()
    }


    override fun onWebHookEvent(scriptInfo: String?) {
        Log.i(TAG, "onWebHookEvent() scriptInfo = $scriptInfo")
        if (scriptInfo==null) return
        try {
            val parenStart = scriptInfo.indexOf('(')
            val methodName = scriptInfo.substring(0, parenStart)

            Log.i(TAG, "onWebHookEvent() methodName = $methodName")

            val parenEnd = scriptInfo.lastIndexOf(')')
            val argument = scriptInfo.substring(parenStart + 1, parenEnd)

            Log.i(TAG, "onWebHookEvent() argument = $argument")

            val inputObject = JSONObject(argument)
            if (inputObject.has("chatbot_id")) {
                reveChatBotId = inputObject.getString("chatbot_id")
            }

            if (inputObject.has("session_id")) {
                reveSessionId = inputObject.getString("session_id")
            }

            Log.i(TAG, "onWebHookEvent() chatBotId = $reveChatBotId")
            Log.i(TAG, "onWebHookEvent() sessionId = $reveSessionId")

            if (!TextUtils.isEmpty(reveChatBotId) && !TextUtils.isEmpty(reveSessionId)) {
                saveReveChatBotSessionInfo()
                callGetGlobalRESTApi(reveChatBotId!!, reveSessionId!!)
            }
        } catch (exception: Exception) {
            Log.e(TAG, "exception in onWebHookEvent() = $exception")
        }
    }

    override fun onBotConversationFinished() {
        reveChatBotId = ""
        reveSessionId = ""
        saveReveChatBotSessionInfo()
    }


    private fun callGetGlobalRESTApi(botId: String, sessionId: String) {
        Log.i(TAG, "callGetGlobalRESTApi() ++")

        val accessToken: String = ACCESS_TOKEN
        Log.i(TAG, "accessToken = $accessToken")

        if (accessToken != null && !TextUtils.isEmpty(accessToken)) {
            val userService = userService
            val call = userService.verifyToken(accessToken, "Reve")
            call.enqueue(object : Callback<TokenVerifyResponse?> {
                override fun onResponse(
                    call: Call<TokenVerifyResponse?>,
                    response: Response<TokenVerifyResponse?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val tokenResponse = response.body()
                        Log.d(TAG, "Status: = " + tokenResponse!!.status)
                        Log.d(TAG, "Payload: = " + tokenResponse!!.payload)

                        val gson = Gson()
                        val payloadJson = gson.toJson(tokenResponse)
                        handleWebHookEvent(botId, sessionId, payloadJson)
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Error getting payload json",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<TokenVerifyResponse?>, t: Throwable) {
                    Log.e(TAG, "Failure: " + t.message)
                    Toast.makeText(this@MainActivity, "API onFailure", Toast.LENGTH_LONG).show()
                }
            })
        } else {
            Log.e(TAG, "accessToken null or empty")
            Toast.makeText(this@MainActivity, "Access token is empty or null", Toast.LENGTH_LONG)
                .show()
        }

        Log.i(TAG, "callGetGlobalRESTApi() --")
    }


    private fun handleWebHookEvent(botId: String, sessionId: String, payload: String?) {
        Log.i(TAG, "handleWebHookEvent()++")
        try {
            val reveChatWebHook = REVEChatWebHook.INSTANCE
            val loginStatus: Boolean = getLoggedInStatus()
            Log.i(TAG, "handleWebHookEvent() loginStatus = $loginStatus")

            reveChatWebHook.setLoggedIn(loginStatus)
            val data = if (payload == null) {
                JSONObject() // empty data
            } else {
                JSONObject(payload)
            }
            Log.i(TAG, "handleWebHookEvent() data = $data")

            reveChatWebHook.setData(data)

            REVEChatWebHook.INSTANCE.loginToChatbot(botId, sessionId)
        } catch (e: java.lang.Exception) {
            Log.i(TAG, "handleWebHookEvent() exception = $e")
        }
        Log.i(TAG, "handleWebHookEvent()--")
    }


    private fun getLoggedInStatus(): Boolean {
        // TODO: need to replace with actual implementation
        return loginStateCheckBox.isChecked()
    }

    private fun dummyChangeLoginStateHandler() {
        if (!TextUtils.isEmpty(reveChatBotId) && !TextUtils.isEmpty(reveSessionId)) {
            // Need to call web hook api again
            callGetGlobalRESTApi(reveChatBotId!!, reveSessionId!!)
        }
    }


}