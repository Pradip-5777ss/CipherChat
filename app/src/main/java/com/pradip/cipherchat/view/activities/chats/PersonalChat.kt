package com.pradip.cipherchat.view.activities.chats

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pradip.cipherchat.R
import com.pradip.cipherchat.adapters.PersonalChatsAdapter
import com.pradip.cipherchat.databinding.ActivityPersonalChatBinding
import com.pradip.cipherchat.model.Message
import java.util.*
import kotlin.collections.HashMap

private lateinit var personalChatBinding: ActivityPersonalChatBinding
private lateinit var chatAdapter: PersonalChatsAdapter
private lateinit var messages: ArrayList<Message>
private lateinit var senderRoom: String
private lateinit var receiverRoom: String
private lateinit var database: FirebaseDatabase
private lateinit var userName: String
private lateinit var receiverId: String
private lateinit var senderId: String
private lateinit var profileImage: String

class PersonalChat : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        personalChatBinding = ActivityPersonalChatBinding.inflate(layoutInflater)
        setContentView(personalChatBinding.root)

        messages = ArrayList()
        chatAdapter = PersonalChatsAdapter(this, messages)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        personalChatBinding.PersonalChatRecyclerView.layoutManager = layoutManager
        personalChatBinding.PersonalChatRecyclerView.adapter = chatAdapter
        personalChatBinding.PersonalChatRecyclerView.setHasFixedSize(true)

        userName = intent.getStringExtra("name").toString()
        receiverId = intent.getStringExtra("uid").toString()
        profileImage = intent.getStringExtra("profilePhoto").toString()
        senderId = FirebaseAuth.getInstance().uid.toString()
        database = FirebaseDatabase.getInstance()

        initButtonClick()

        personalChatBinding.PersonalChatUserName.text = userName
        Glide.with(applicationContext).load(profileImage).placeholder(R.drawable.person_account)
            .into(personalChatBinding.PersonalChatProfileImage)

        senderRoom = senderId + receiverId
        receiverRoom = receiverId + senderId

        database.reference.child("Chats").child(senderRoom).child("Messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messages.clear()
                    for (textMessageSnapshot: DataSnapshot in snapshot.children) {
                        val message = textMessageSnapshot.getValue(Message::class.java)
                        if (message != null) {
                            messages.add(message)
                        }
                        Log.d("Personal chat", "Message send successfully")
                    }
                    chatAdapter.notifyDataSetChanged()
                    personalChatBinding.PersonalChatRecyclerView.smoothScrollToPosition(
                        (personalChatBinding.PersonalChatRecyclerView.adapter as PersonalChatsAdapter).itemCount
                    )
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Personal chat", "Message not send")
                }

            })

    }

    private fun initButtonClick() {
        personalChatBinding.PersonalChatBackBtn.setOnClickListener {
            finish()
        }

        personalChatBinding.PersonalChatSendBtn.setOnClickListener {
            val textMessage = personalChatBinding.PersonalChatEditText.text.toString().trim()

            val date = Date()
            val message = Message(textMessage, senderId, date.time)
            val randomKey: String? = database.reference.push().key

            val lastMessageObj:HashMap<String,Any> = HashMap()
            lastMessageObj["lastMessage"] = message.getMessage()
            lastMessageObj["lastMessageTime"] = date.time

            database.reference.child("Chats").child(senderRoom).updateChildren(lastMessageObj)
            database.reference.child("Chats").child(receiverRoom).updateChildren(lastMessageObj)

            personalChatBinding.PersonalChatEditText.setText("")
            if (randomKey != null) {
                database.reference.child("Chats").child(senderRoom).child("Messages")
                    .child(randomKey)
                    .setValue(message)
                    .addOnSuccessListener {
                        database.reference.child("Chats").child(receiverRoom).child("Messages")
                            .child(randomKey)
                            .setValue(message)
                            .addOnSuccessListener {

                            }
                    }
            }
        }

    }
}