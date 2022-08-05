package com.pradip.cipherchat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.pradip.cipherchat.model.UserPersonalDetails;
import com.pradip.cipherchat.model.UserStatus;

import java.util.Objects;

public class MainActivityUserStoriesAdapter extends FirebaseRecyclerAdapter<UserStatus, MainActivityUserStoriesAdapter.userStatusViewHolder> {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    public MainActivityUserStoriesAdapter(@NonNull FirebaseRecyclerOptions<UserStatus> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull userStatusViewHolder holder, int position, @NonNull UserStatus model) {

        userInfo(holder, currentUserId, position);
        if (holder.getAbsoluteAdapterPosition() == 0) {
            myStory(holder.addStatusText, holder.statusAddBtn, false);
        }

    }

    private void myStory(TextView addStatusText, ImageView statusAddBtn, boolean click) {

    }

    private void userInfo(userStatusViewHolder holder, String currentUserId, int position) {
        database.getReference().child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserPersonalDetails userDetails = snapshot.getValue(UserPersonalDetails.class);
                if (position == 0) {
                    Glide.with(holder.statusAdd.getContext()).load(Objects.requireNonNull(userDetails).getProfileImage()).placeholder(R.drawable.person_account).into(holder.statusAdd);
                } else {
                    Glide.with(holder.statusSeen.getContext()).load(Objects.requireNonNull(userDetails).getProfileImage()).placeholder(R.drawable.person_account).into(holder.statusSeen);
                    holder.otherStatusText.setText(userDetails.getFullName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(holder.itemView.getContext(), "Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @NonNull
    @Override
    public userStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_add_status, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_status, parent, false);
        }
        return new userStatusViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    public static class userStatusViewHolder extends RecyclerView.ViewHolder {
        ImageView statusAdd, statusSeen, statusAddBtn, statusCircularImage;
        TextView addStatusText, otherStatusText;

        public userStatusViewHolder(@NonNull View itemView) {
            super(itemView);
            statusSeen = itemView.findViewById(R.id.status_seen);
            statusAddBtn= itemView.findViewById(R.id.Add_Status_Circular_Image_AddBTN);
            statusCircularImage = itemView.findViewById(R.id.Status_Circular_Image);
            statusAdd = itemView.findViewById(R.id.Add_Status_Circular_Image);
            addStatusText = itemView.findViewById(R.id.Add_Status_UserName);
            otherStatusText = itemView.findViewById(R.id.Status_UserName);
        }
    }
}
