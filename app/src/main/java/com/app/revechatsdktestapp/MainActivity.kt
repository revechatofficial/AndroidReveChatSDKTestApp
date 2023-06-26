package com.app.revechatsdktestapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.revesoft.revechatsdk.model.VisitorInfo
import com.revesoft.revechatsdk.state.LoginState
import com.revesoft.revechatsdk.ui.activity.ReveChatActivity
import com.revesoft.revechatsdk.utils.ReveChat

class MainActivity : AppCompatActivity() {

    private lateinit var accountIdEditText: EditText
    private lateinit var userNameEditText: EditText
    private lateinit var emailIdEditText: EditText
    private lateinit var phoneNumberEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        accountIdEditText = findViewById(R.id.accountID)
        userNameEditText = findViewById(R.id.userName)
        emailIdEditText = findViewById(R.id.userEmail)
        phoneNumberEditText = findViewById(R.id.userPhone)

        findViewById<Button>(R.id.startChat).setOnClickListener {
            startChat()
        }
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

        val visitorInfo: VisitorInfo = VisitorInfo.Builder()
            .name(name)
            .email(email)
            .phoneNumber(phone)
            .appLoginState(LoginState.LOGGED_OUT)       // pass LoginState.LOGGED_IN if you don't want to show pre-chat form
            .build()


        ReveChat.setVisitorInfo(visitorInfo)

        startActivity(Intent(this, ReveChatActivity::class.java))
    }
}