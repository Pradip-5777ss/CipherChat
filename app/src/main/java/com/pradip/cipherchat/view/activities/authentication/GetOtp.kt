package com.pradip.cipherchat.view.activities.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pradip.cipherchat.databinding.ActivityGetOtpBinding
import com.pradip.cipherchat.view.activities.MainActivity
import java.util.concurrent.TimeUnit

class GetOtp : AppCompatActivity() {

    private lateinit var getOtpBinding: ActivityGetOtpBinding
    private lateinit var userPhoneNumber: String
    private lateinit var userCountry: String
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mVerificationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getOtpBinding = ActivityGetOtpBinding.inflate(layoutInflater)
        setContentView(getOtpBinding.root)

        mAuth = FirebaseAuth.getInstance()
        val user = Firebase.auth.currentUser
        if (user != null) {
            startActivity(Intent(this@GetOtp, MainActivity::class.java))
        }

        // Keyboard auto focus when activity start
        getOtpBinding.OtpVerificationPinView.requestFocus()

        // Get the phone number from previous activity and set it in textview
        userPhoneNumber = intent.getStringExtra("Mobile_Number").toString()
        getOtpBinding.ShowPhoneNumber.text = userPhoneNumber

        userCountry = intent.getStringExtra("Country_Name").toString()

        startPhoneNumberVerification(userPhoneNumber)

        getOtpBinding.OTPVerifyNextBtn.setOnClickListener {
            val code = getOtpBinding.OtpVerificationPinView.text.toString()
            if (code.isNotEmpty()) {
                verifyPhoneNumberWithCode(mVerificationId, code)
            }
        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                val code: String? = credential.smsCode
                if (code != null) {
                    getOtpBinding.OtpVerificationPinView.setText(code)
                    verifyPhoneNumberWithCode(mVerificationId, code)
                }
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                Toast.makeText(this@GetOtp, exception.message, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                s: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(s, forceResendingToken)
                mVerificationId = s
            }
        }

    private fun verifyPhoneNumberWithCode(verificationId: String, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "Verification Complete", Toast.LENGTH_SHORT)
                    .show()
//                    val user: FirebaseUser? = task.result.user
                startActivity(Intent(this@GetOtp, UserInfoSetup::class.java))
                finishAffinity()
            } else {
                Toast.makeText(applicationContext, "Verification failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}