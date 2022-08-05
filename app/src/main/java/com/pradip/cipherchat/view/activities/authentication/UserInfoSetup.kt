package com.pradip.cipherchat.view.activities.authentication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.pradip.cipherchat.R
import com.pradip.cipherchat.databinding.ActivityUserInfoSetupBinding
import com.pradip.cipherchat.model.UserPersonalDetails
import com.pradip.cipherchat.view.activities.MainActivity

class UserInfoSetup : AppCompatActivity() {

    private lateinit var userInfoSetupBinding: ActivityUserInfoSetupBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var selectedImage: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userInfoSetupBinding = ActivityUserInfoSetupBinding.inflate(layoutInflater)
        setContentView(userInfoSetupBinding.root)

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        initButtonClick()
    }

    private fun initButtonClick() {
        userInfoSetupBinding.UserInfoSetupCameraBtn.setOnClickListener {
            showBottomSheetImagePicker()
        }

        userInfoSetupBinding.ProfileSetupNextBtn.setOnClickListener {
            val firstName =
                userInfoSetupBinding.SetupProfileFirstNameEdittext.layout.text.toString().trim()
            val lastName = userInfoSetupBinding.SetupProfileLastNameEdittext.layout.text.toString().trim()

            val fullName = "$firstName $lastName"
            if (firstName.isEmpty()) {
                userInfoSetupBinding.SetupProfileFirstNameEdittext.error = "Please type a name"
                return@setOnClickListener
            }

            val reference: StorageReference? =
                auth.uid?.let { it1 -> storage.reference.child("Profiles").child(it1) }
            reference?.putFile(selectedImage)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    reference.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl: String = uri.toString()

                        val uid: String? = auth.uid
                        val phoneNumber: String? = auth.currentUser?.phoneNumber

                        val user =
                            UserPersonalDetails(uid, fullName, phoneNumber, imageUrl, "")

                        if (uid != null) {
                            database.reference.child("Users").child(uid)
                                .setValue(user)
                                .addOnSuccessListener {
                                    val intent =
                                        Intent(applicationContext, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                        } else {
                            Toast.makeText(applicationContext, "Error Occurred", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
                } else {
                    val uid: String? = auth.uid
                    val phoneNumber: String? = auth.currentUser?.phoneNumber

                    val user =
                        UserPersonalDetails(uid, fullName, phoneNumber, "No Image", "")

                    if (uid != null) {
                        database.reference.child("Users").child(uid)
                            .setValue(user)
                            .addOnSuccessListener {
                                val intent =
                                    Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                    } else {
                        Toast.makeText(applicationContext, "Error Occurred", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    /**
     * Method to open bottom sheet picker on camera button click
     */
    @SuppressLint("InflateParams")
    private fun showBottomSheetImagePicker() {
        val bottomSheetImageView = layoutInflater.inflate(R.layout.bottom_sheet_image_picker, null)

        bottomSheetImageView.findViewById<View>(R.id.in_gallery).setOnClickListener {
            openGallery()
            bottomSheetDialog.dismiss()
        }

        bottomSheetImageView.findViewById<View>(R.id.in_camera).setOnClickListener {
            openCamera()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetImageView)

        bottomSheetDialog.show()
    }

    /**
     * Method to open camera
     */
    private fun openCamera() {

    }

    /**
     * Method to open gallery
     */
    private fun openGallery() {
        Toast.makeText(this, "Gallery open", Toast.LENGTH_SHORT).show()

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)
    }

    private val galleryActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {

                userInfoSetupBinding.profileCircleImage.setImageURI(result.data!!.data)
                selectedImage = result.data!!.data!!

            } else {
                Toast.makeText(applicationContext, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
}