package com.pradip.cipherchat.view.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.pradip.cipherchat.R
import com.pradip.cipherchat.adapters.MainActivityChatListAdapter
import com.pradip.cipherchat.adapters.MainActivityStatusAdapter
import com.pradip.cipherchat.databinding.ActivityMainBinding
import com.pradip.cipherchat.model.ChatList
import com.pradip.cipherchat.model.UserPersonalDetails
import com.pradip.cipherchat.model.UserStatus

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var firebaseUser: FirebaseUser? = null
    private lateinit var database: FirebaseDatabase
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        // Database instance
        database = FirebaseDatabase.getInstance()


        // Bottom Navigation view fragments
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        navController = navHostFragment.navController
        setupWithNavController(mainBinding.MainActivityBottomNavBar, navController)

    }

}