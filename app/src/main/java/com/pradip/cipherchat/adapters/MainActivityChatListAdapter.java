package com.pradip.cipherchat.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pradip.cipherchat.R;
import com.pradip.cipherchat.databinding.SingleUserLayoutBinding;
import com.pradip.cipherchat.model.ChatList;
import com.pradip.cipherchat.view.activities.chats.PersonalChat;

import java.text.SimpleDateFormat;

public class MainActivityChatListAdapter extends FirebaseRecyclerAdapter<ChatList, MainActivityChatListAdapter.chatListViewHolder> {

    public MainActivityChatListAdapter(@NonNull FirebaseRecyclerOptions<ChatList> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull chatListViewHolder holder, int position, @NonNull ChatList model) {

        String senderId = FirebaseAuth.getInstance().getUid();
        String senderRoom = senderId + model.getUid();

        FirebaseDatabase.getInstance().getReference().child("Chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String lastMessage = snapshot.child("lastMessage").getValue(String.class);
                    Long lastMessageTime = snapshot.child("lastMessageTime").getValue(Long.class);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                    String msgTime = dateFormat.format(lastMessageTime);
                    holder.singleUserBinding.SingleUserMessageTime.setText(msgTime);
                    holder.singleUserBinding.SingleUserLastMessage.setText(lastMessage);
                } else {
                    holder.singleUserBinding.SingleUserLastMessage.setText(R.string.Message_Description);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.singleUserBinding.SingleUserName.setText(model.getFullName());
        Glide.with(holder.singleUserBinding.SingleUserProfileImage.getContext()).load(model.getProfileImage()).placeholder(R.drawable.person_account).into(holder.singleUserBinding.SingleUserProfileImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PersonalChat.class);
            intent.putExtra("name", model.getFullName());
            intent.putExtra("uid", model.getUid());
            intent.putExtra("profilePhoto", model.getProfileImage());
            v.getContext().startActivity(intent);

        });
    }

    @NonNull
    @Override
    public chatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_layout, parent, false);
        return new chatListViewHolder(view);
    }

    public static class chatListViewHolder extends RecyclerView.ViewHolder {

        SingleUserLayoutBinding singleUserBinding;

        public chatListViewHolder(@NonNull View itemView) {
            super(itemView);
            singleUserBinding = SingleUserLayoutBinding.bind(itemView);
        }
    }
}
