package com.app.revechatsdktestapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.revesoft.revechatsdk.model.VisitorInfo
import com.revesoft.revechatsdk.service.REVEChatApiService
import com.revesoft.revechatsdk.state.LoginState
import com.revesoft.revechatsdk.ui.activity.ReveChatActivity
import com.revesoft.revechatsdk.utils.ReveChat
import com.revesoft.revechatsdk.webrtc.WebRTCHandler


class MainActivity : AppCompatActivity() {

    private lateinit var accountIdEditText: EditText
    private lateinit var userNameEditText: EditText
    private lateinit var emailIdEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var loginStateCheckBox : CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        accountIdEditText = findViewById(R.id.accountID)
        userNameEditText = findViewById(R.id.userName)
        emailIdEditText = findViewById(R.id.userEmail)
        phoneNumberEditText = findViewById(R.id.userPhone)
        loginStateCheckBox = findViewById(R.id.login_state_checkbox)

        findViewById<Button>(R.id.startChat).setOnClickListener {
            startChat()
        }

        findViewById<Button>(R.id.logOut).setOnClickListener {
            logout()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!REVEChatApiService.isCallRunning())
            stopService(Intent(this, REVEChatApiService::class.java))
    }


    private fun logout() {
//        val intent = Intent(REVEChatApiService.REVECHAT_SDK_INTENT_FILTER)
//        intent.putExtra(REVEChatApiService.LOGOUT_MESSAGE, "")
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun startChat() {
        val accountId = accountIdEditText.text.toString()
        val name = userNameEditText.text.toString()
        val email = emailIdEditText.text.toString()
        val phone = phoneNumberEditText.text.toString()

        if (TextUtils.isEmpty(accountId) || TextUtils.isEmpty(name) || TextUtils.isEmpty(email)) {
            Toast.makeText(this@MainActivity, "Please fill up all required information", Toast.LENGTH_LONG).show()
            return
        }

        ReveChat.init(accountId)


//        var loginState : LoginState = LoginState.LOGGED_OUT
//        var doNotShowPreChatForm = true  // dummy variable to demonstrate showing and not showing pre-chat form
//
//        /**
//         * if application don't need to show pre-chat form then need to set as
//         *      loginState = LoginState.LOGGED_IN
//         */
//        if (doNotShowPreChatForm)
//            loginState = LoginState.LOGGED_IN


        var loginState : LoginState = LoginState.LOGGED_OUT
//        var doNotShowPreChatForm = true  // dummy variable to demonstrate showing and not showing pre-chat form

        if (loginStateCheckBox.isChecked)
            loginState = LoginState.LOGGED_IN


        val visitorInfo: VisitorInfo = VisitorInfo.Builder()
            .name(name)
            .email(email)
            .phoneNumber(phone)
            .appLoginState(loginState)
            .build()


        ReveChat.setVisitorInfo(visitorInfo)

        val intent = Intent(this, ReveChatActivity::class.java)
        resultLauncher.launch(intent)
//      startActivity(Intent(this, ReveChatActivity::class.java))
        startService(Intent(this, REVEChatApiService::class.java))

    }


    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->
        if (result.resultCode == Activity.RESULT_OK) {
//          val resultData = result.data?.getStringExtra("key")
            Toast.makeText(this, "RESULT_OK", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Operation Canceled", Toast.LENGTH_SHORT).show()
        }
    }

}