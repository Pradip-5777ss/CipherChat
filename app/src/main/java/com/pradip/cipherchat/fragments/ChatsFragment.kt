package com.pradip.cipherchat.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.pradip.cipherchat.R
import com.pradip.cipherchat.adapters.MainActivityChatListAdapter
import com.pradip.cipherchat.adapters.MainActivityStatusAdapter
import com.pradip.cipherchat.adapters.MainActivityUserStoriesAdapter
import com.pradip.cipherchat.model.*
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList

class ChatsFragment : Fragment() {

    private lateinit var chatsRecyclerView: ShimmerRecyclerView
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var adapter: MainActivityChatListAdapter
    private lateinit var statusRecyclerView: RecyclerView
    private lateinit var statusAdapter: MainActivityStatusAdapter
    private lateinit var userStoriesAdapter: MainActivityUserStoriesAdapter

    private lateinit var database: FirebaseDatabase
    private lateinit var userPersonalDetails: UserPersonalDetails

    override fun onStart() {
        super.onStart()
        chatsRecyclerView.recycledViewPool.clear()
        statusRecyclerView.recycledViewPool.clear()
        adapter.notifyDataSetChanged()
        userStoriesAdapter.notifyDataSetChanged()
        adapter.startListening()
        userStoriesAdapter.startListening()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_chats, container, false)

        chatsRecyclerView = view.findViewById(R.id.Chats_Fragment_Recyclerview)
        statusRecyclerView = view.findViewById(R.id.Fragment_Status_RecyclerView)
        statusRecyclerView.setHasFixedSize(true)

//        userStatuses = ArrayList()
        database = FirebaseDatabase.getInstance()
//        statusRoot = database.reference.child("Stories").child()
//        if (FirebaseAuth.getInstance().currentUser != null) {
//            database.reference.child("Users").child(FirebaseAuth.getInstance().uid!!)
//                .addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        userPersonalDetails = snapshot.getValue(UserPersonalDetails::class.java)!!
////                    Glide.with(requireContext()).load(userPersonalDetails.profileImage)
////                        .placeholder(R.drawable.person_account).into(circleImage)
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                    }
//
//                })
//        }


        //  show status
        val statusOptions: FirebaseRecyclerOptions<UserStatus> =
            FirebaseRecyclerOptions.Builder<UserStatus>()
                .setQuery(
                    FirebaseDatabase.getInstance().reference.child("Stories"),
                    UserStatus::class.java
                )
                .build()
        userStoriesAdapter = MainActivityUserStoriesAdapter(statusOptions)
        val layoutManager = LinearLayoutManager(view.context)
        layoutManager.orientation = RecyclerView.HORIZONTAL
        statusRecyclerView.layoutManager = layoutManager
        statusRecyclerView.adapter = userStoriesAdapter

//        statusAdapter = MainActivityStatusAdapter(context,userStatuses)
//        statusRecyclerView.adapter = statusAdapter
//        statusAdapter = MainActivityStatusAdapter(view.context, userStatuses)
//        val layoutManager = LinearLayoutManager(view.context)
//        layoutManager.orientation = RecyclerView.HORIZONTAL
//        statusRecyclerView.layoutManager = layoutManager
//        statusRecyclerView.adapter = statusAdapter

//        circleImage.setOnClickListener {
//            showBottomSheetImagePicker()
//        }

//        database.reference.child("Stories").addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//                    userStatuses.clear()
//                    for (storySnapshot: DataSnapshot in snapshot.children) {
//                        val status = UserStatus()
//                        status.setName(storySnapshot.child("UserName").getValue(String::class.java))
//                        status.setProfileImage(
//                            storySnapshot.child("UserProfileImage").getValue(String::class.java)
//                        )
//                        status.setLastUpdated(
//                            storySnapshot.child("lastUpdated").getValue(Long::class.java)!!
//                        )
//
//                        val userPersonalStatuses: ArrayList<Status> = ArrayList()
//                        for (statusSnapshot: DataSnapshot in storySnapshot.child("Statuses").children) {
//                            val sampleStatus: Status = statusSnapshot.getValue(Status::class.java)!!
//                            userPersonalStatuses.add(sampleStatus)
//                        }
//
//                        status.setStatuses(userPersonalStatuses)
//
//                        userStatuses.add(status)
//
//
//                    }
//                    statusAdapter.notifyDataSetChanged()
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//
//        })

        //  Chat list from firebase
        val options: FirebaseRecyclerOptions<ChatList> =
            FirebaseRecyclerOptions.Builder<ChatList>()
                .setQuery(
                    FirebaseDatabase.getInstance().reference.child("Users"),
                    ChatList::class.java
                )
                .build()

        adapter = MainActivityChatListAdapter(options)
//        chatsRecyclerView.hideShimmerAdapter()
        chatsRecyclerView.adapter = adapter
//        chatsRecyclerView.hideShimmerAdapter()

        return view
    }

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

        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomSheetImageView)

        bottomSheetDialog.show()
    }

    private fun openCamera() {

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)
    }

    private val galleryActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {

                val image = result.data
                val storage: FirebaseStorage = FirebaseStorage.getInstance()
                val date = Date()
                val storageReference =
                    storage.reference.child("Status").child(date.time.toString() + "")

                storageReference.putFile(image!!.data!!).addOnCompleteListener { it ->
                    if (it.isSuccessful) {
                        storageReference.downloadUrl.addOnSuccessListener {
                            val userStatus = UserStatus()
                            userStatus.setName(userPersonalDetails.fullName)
                            userStatus.setProfileImage(userPersonalDetails.profileImage)
                            userStatus.setLastUpdated(date.time)

                            val map: HashMap<String, Any> = HashMap()
                            map["UserName"] = userStatus.getName()
                            map["UserProfileImage"] = userStatus.getProfileImage()
                            map["lastUpdated"] = userStatus.getLastUpdated()

                            val status = Status(it.toString(), userStatus.getLastUpdated())

                            database.reference.child("Stories")
                                .child(FirebaseAuth.getInstance().uid!!).updateChildren(map)

                            database.reference.child("Stories")
                                .child(FirebaseAuth.getInstance().uid!!).child("Statuses").push()
                                .setValue(status)

                            Toast.makeText(
                                requireContext(),
                                "Download successful",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            } else {
                Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

}