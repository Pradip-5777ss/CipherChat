package com.pradip.cipherchat.view.activities.authentication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pradip.cipherchat.databinding.ActivityPhoneNumberVerificationBinding

class PhoneNumberVerification : AppCompatActivity() {

    private lateinit var phoneNumberVerificationBind: ActivityPhoneNumberVerificationBinding
    private lateinit var userCompletePhoneNumber: String
    private lateinit var userCountryName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        phoneNumberVerificationBind = ActivityPhoneNumberVerificationBinding.inflate(layoutInflater)
        setContentView(phoneNumberVerificationBind.root)

        // Keyboard auto focus when activity start
        phoneNumberVerificationBind.phoneNumberEdittext.requestFocus()

        /**
         *  Button to move next activity
         */
        phoneNumberVerificationBind.PhoneNumberVerifyNextBtn.setOnClickListener {

            // Get the phone number from the user
            val userPhoneNumber = phoneNumberVerificationBind.phoneNumberEdittext.text.toString()

            // Check that the number is valid or not
            if (!validateMobileNumber(userPhoneNumber)) {
                return@setOnClickListener
            }

            phoneNumberVerificationBind.ccp.registerCarrierNumberEditText(
                phoneNumberVerificationBind.phoneNumberEdittextLayout.editText
            )

            // Get user complete phone number & selected country name from country code picker
            userCompletePhoneNumber = phoneNumberVerificationBind.ccp.fullNumberWithPlus.trim()
            userCountryName = phoneNumberVerificationBind.ccp.selectedCountryName

            // Create intent for transfer the data from this activity to another activity
            val intent = Intent(this, GetOtp::class.java)
            intent.putExtra("Mobile_Number", userCompletePhoneNumber)
            intent.putExtra("Country_Name", userCountryName)
            startActivity(intent)
            finish()
        }
    }

    /**
     * Function to check that the phone number is valid or not
     * @param number phone number for validation
     * @return Boolean Either `true` or `false`
     */
    private fun validateMobileNumber(number: String): Boolean {
        val checkSpaces = "\\A\\w{1,20}\\z"
        return if (number.isEmpty()) {
            phoneNumberVerificationBind.phoneNumberEdittextLayout.error = "Enter valid phone number"
            false
        } else if (number.length != 10) {
            phoneNumberVerificationBind.phoneNumberEdittextLayout.error =
                "Phone number is not valid"
            false
        } else if (!number.matches(checkSpaces.toRegex())) {
            phoneNumberVerificationBind.phoneNumberEdittextLayout.error =
                "No white space are allowed"
            false
        } else {
            phoneNumberVerificationBind.phoneNumberEdittextLayout.error = null
            phoneNumberVerificationBind.phoneNumberEdittextLayout.isErrorEnabled = false
            true
        }
    }
}
