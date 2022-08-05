package com.pradip.cipherchat.view.activities.startup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pradip.cipherchat.databinding.ActivitySplashScreenBinding
import com.pradip.cipherchat.view.activities.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    // Bind the activity
    private lateinit var splashScreenBinding: ActivitySplashScreenBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(splashScreenBinding.root)

        /* Firebase instance create */
        mAuth = FirebaseAuth.getInstance()
        val user = Firebase.auth.currentUser

        /* Condition check to transfer activity */
        if (user != null) {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                finish()
            }, 3000)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this@SplashScreen, WelcomeScreen::class.java))
                finish()
            }, 3000)
        }
    }
}