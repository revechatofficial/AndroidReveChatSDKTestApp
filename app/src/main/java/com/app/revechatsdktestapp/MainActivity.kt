package com.app.revechatsdktestapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.revesoft.revechatsdk.model.VisitorInfo
import com.revesoft.revechatsdk.service.REVEChatApiService
import com.revesoft.revechatsdk.ui.activity.ReveChatActivity
import com.revesoft.revechatsdk.utils.ReveChat


class MainActivity : AppCompatActivity() {

    private lateinit var accountIdEditText: EditText
    private lateinit var userNameEditText: EditText
    private lateinit var emailIdEditText: EditText
    private lateinit var phoneNumberEditText: EditText


    private val accountId = "2552651"
    private val userName = "androidSDK"
    private val userEmail = "androidSDK@test.com"
    private val userPhone = "01814123123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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



        findViewById<Button>(R.id.startChat).setOnClickListener {
            startChat()
        }

        // this need to be called
        initiateReveChat()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!REVEChatApiService.isCallRunning())
            stopService(Intent(this, REVEChatApiService::class.java))
    }


    private fun initiateReveChat() {
        // test access token value
        val accessToken =
            "Basic eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHRlcm5hbF9zeXN0ZW1fZW1haWwiOiJ0ZXN0cUBnbWFpbC5jb20iLCJmaXJzdE5hbWUiOiIiLCJsYXN0TmFtZSI6IiIsImNvbXBhbnlfaWQiOjExMjksInVzZXJfbmFtZSI6InRlc3RxQGdtYWlsLmNvbSIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE3NDM4MjYyNDQsImF1dGhvcml0aWVzIjpbImNyZXciXSwianRpIjoiNjIwZmY2OWEtYjc3OS00ZjgzLThkMTEtOTA5MzBhYjc2OGMzIiwiY2xpZW50X2lkIjoid2ViIn0.QNGrQeEDUQyhAyqrg9SASVXxHxcbq62gNxR73-CsrTnn_lyufdoHr8-5UHQyrXO4ddUq9S3DNonp8VgrdiuH_oR2dwTZFy9mmwT7RbXcL33lke1hBitCIVLMXR8qyNPfBtRXTYgKYvbQVFu9hA3nlMBj4iFQb8VpAgYCo2isuFDdsTyV1T8Oevd40aecrP1PAImKVdNQVr9zJ9d-mhIrreo-4lxuhUC5cbHw1sC7xQkwk5rLWP8cMvxEBxizZVtuAoFsnjZTasMZXl6VMk61RR77fRpfbmWvmeG2FlQOiTBqlejh1Hv74ykuB9J0O18yTBKHxcwBuJUwACL81gWUOg"

        ReveChat.init(accountId)

        //create visitor info
        val visitorInfo: VisitorInfo = VisitorInfo.Builder()
            .name(userName) // set name
            .email(userEmail)
            .phoneNumber(userPhone)
            .build()

        visitorInfo.accessToken = accessToken // set access token if any

        ReveChat.setVisitorInfo(visitorInfo)

        ReveChat.setAppBundleName(BuildConfig.APPLICATION_ID)
        ReveChat.setAppVersionNumber(BuildConfig.VERSION_NAME)
        startService(Intent(this, REVEChatApiService::class.java))
    }



    private fun startChat() {
        val accessToken = "Basic eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHRlcm5hbF9zeXN0ZW1fZW1haWwiOiJ0ZXN0cUBnbWFpbC5jb20iLCJmaXJzdE5hbWUiOiIiLCJsYXN0TmFtZSI6IiIsImNvbXBhbnlfaWQiOjExMjksInVzZXJfbmFtZSI6InRlc3RxQGdtYWlsLmNvbSIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE3NDM4MjYyNDQsImF1dGhvcml0aWVzIjpbImNyZXciXSwianRpIjoiNjIwZmY2OWEtYjc3OS00ZjgzLThkMTEtOTA5MzBhYjc2OGMzIiwiY2xpZW50X2lkIjoid2ViIn0.QNGrQeEDUQyhAyqrg9SASVXxHxcbq62gNxR73-CsrTnn_lyufdoHr8-5UHQyrXO4ddUq9S3DNonp8VgrdiuH_oR2dwTZFy9mmwT7RbXcL33lke1hBitCIVLMXR8qyNPfBtRXTYgKYvbQVFu9hA3nlMBj4iFQb8VpAgYCo2isuFDdsTyV1T8Oevd40aecrP1PAImKVdNQVr9zJ9d-mhIrreo-4lxuhUC5cbHw1sC7xQkwk5rLWP8cMvxEBxizZVtuAoFsnjZTasMZXl6VMk61RR77fRpfbmWvmeG2FlQOiTBqlejh1Hv74ykuB9J0O18yTBKHxcwBuJUwACL81gWUOg"

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
            .accessToken(accessToken)
            .build()


        ReveChat.setVisitorInfo(visitorInfo)

//      val intent = Intent(this, ReveChatActivity::class.java)
//      resultLauncher.launch(intent)
        startActivity(Intent(this, ReveChatActivity::class.java))
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

}