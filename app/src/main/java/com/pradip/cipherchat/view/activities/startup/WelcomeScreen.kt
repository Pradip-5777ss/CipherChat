package com.pradip.cipherchat.view.activities.startup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pradip.cipherchat.databinding.ActivityWelcomeScreenBinding
import com.pradip.cipherchat.view.activities.authentication.PhoneNumberVerification

class WelcomeScreen : AppCompatActivity() {

    private lateinit var welcomeScreenBinding: ActivityWelcomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        welcomeScreenBinding = ActivityWelcomeScreenBinding.inflate(layoutInflater)
        setContentView(welcomeScreenBinding.root)

        welcomeScreenBinding.AgreeBtn.setOnClickListener {
            startActivity(Intent(this@WelcomeScreen, PhoneNumberVerification::class.java))

        }
    }
}