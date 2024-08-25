# AndroidReveChatSDKTestApp
How to add reve chat SDK in a android application


1. In settings.gradle file add following repository

dependencyResolutionManagement {
  repositories {
    maven {
            url "https://jfrog-artifact.revechat.com/artifactory/artifactory/"
     }
    }
}
        
2.  In application build.gradle file, add following dependency 

   dependencies {
           implementation('com.revesoft.revechatsdk:revechatsdk:1.0.14.3.3')
   }
   
   
 
 3. Use following code to initialize and start REVE Chat SDK activity. You will get the the account id by signing up https://app.revechat.com/ from Integration -> Mobile SDK
   
   
For Kotlin
==========

// Initializing with account id
ReveChat.init("account id")


var loginState : LoginState = LoginState.LOGGED_OUT
var doNotShowPreChatForm = true 

/**
 * if application don't need to show pre-chat form then need to set as
 *      loginState = LoginState.LOGGED_IN
 */
if (doNotShowPreChatForm)
  loginState = LoginState.LOGGED_IN

// Creating visitor info
val visitorInfo: VisitorInfo = VisitorInfo.Builder()
	.name("your name")
	.email("your@email.com")
	.phoneNumber("your number")
        .appLoginState(loginState) 
	.build()

// et visitor info
ReveChat.setVisitorInfo(visitorInfo)

// Optional
// If want to Receive push notification from Reve Chat.
// Add your device token id(registration Id)
// You also need to do step 4.
ReveChat.setDeviceTokenId("deviceTokenId")

// starting chat window
startActivity(Intent(this, ReveChatActivity::class.java))


For JAVA
========

// Initializing with account id
ReveChat.init("account id");

LoginState loginState = LoginState.LOGGED_OUT;
boolean doNotShowPreChatForm = true;

/**
 * if application don't need to show pre-chat form then need to set as
 *      loginState = LoginState.LOGGED_IN
 */
if (doNotShowPreChatForm)
  loginState = LoginState.LOGGED_IN;

  
// Creating visitor info
VisitorInfo visitorInfo = new VisitorInfo.Builder()
	.name("your name")
	.email("your@email.com")
	.phoneNumber("your number")
        .appLoginState(loginState)
	.build();
 
// Set visitor info
ReveChat.setVisitorInfo(visitorInfo);
 
// Optional
// If want to Receive push notification from Reve Chat.
// Add your device token id(registration Id)
// You also need to do step 4.
ReveChat.setDeviceTokenId("deviceTokenId");
 
//starting chat window
startActivity(new Intent(this, ReveChatActivity.class));


4. Proguard Configuration
In your proguard configuration file add below

-keep class org.webrtc.** { *; }
-keep class org.webrtc.voiceengine.** { *; }
-dontwarn org.webrtc.**

-keep class com.revesoft.revechatsdk.**{*;}
-keep interface com.revesoft.revechatsdk.* { *; }
-keep enum com.revesoft.revechatsdk.* { *; }
-dontwarn com.revesoft.revechatsdk.**





