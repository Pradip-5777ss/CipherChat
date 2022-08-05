package com.pradip.cipherchat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pradip.cipherchat.R;
import com.pradip.cipherchat.databinding.UserAddStatusBinding;
import com.pradip.cipherchat.databinding.UserStatusBinding;
import com.pradip.cipherchat.model.Status;
import com.pradip.cipherchat.model.Stories;
import com.pradip.cipherchat.model.UserPersonalDetails;
import com.pradip.cipherchat.model.UserStatus;
import com.pradip.cipherchat.view.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class MainActivityStatusAdapter extends RecyclerView.Adapter<MainActivityStatusAdapter.StatusViewHolder> {

    Context context;
    //    ArrayList<Stories> mStory;
    UserStatusBinding statusBinding;
    UserAddStatusBinding addStatusBinding;
    ArrayList<UserStatus> userStatuses;
    String currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    public MainActivityStatusAdapter(Context context, ArrayList<UserStatus> userStatuses) {
        this.context = context;
        this.userStatuses = userStatuses;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.user_add_status, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.user_status, parent, false);
        }
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {

//        UserStatus userStatus = userStatuses.get(position);
//        Stories stories = mStory.get(position);
//        userInfo(stories.getUid(), position);
////        if (holder.getAbsoluteAdapterPosition() != 0){
////
////        }
//        if (holder.getAbsoluteAdapterPosition() == 0) {
//            myStory(addStatusBinding.AddStatusUserName, addStatusBinding.AddStatusCircularImageAddBTN, false);
//        }
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (holder.getAbsoluteAdapterPosition() == 0){
//                    myStory(addStatusBinding.AddStatusUserName,addStatusBinding.AddStatusCircularImageAddBTN,true);
//                }
//            }
//        });
//        UserStatus userStatus = userStatuses.get(position);
//
//        Status lastStatus = userStatus.getStatuses().get(userStatus.getStatuses().size() - 1);
//
//        Glide.with(context).load(lastStatus.getImageUrl()).into(holder.statusBinding.StatusCircularImage);
//
//        holder.statusBinding.circularStatusView.setPortionsCount(userStatus.getStatuses().size());
//        holder.statusBinding.circularStatusView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ArrayList<MyStory> myStories = new ArrayList<>();
//                for (Status status : userStatus.getStatuses()) {
//                    myStories.add(new MyStory(status.getImageUrl()));
//                }
//                new StoryView.Builder(((MainActivity) context).getSupportFragmentManager())
//                        .setStoriesList(myStories) // Required
//                        .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
//                        .setTitleText(userStatus.getName()) // Default is Hidden
//                        .setSubtitleText("") // Default is Hidden
//                        .setTitleLogoUrl(userStatus.getProfileImage()) // Default is Hidden
//                        .setStoryClickListeners(new StoryClickListeners() {
//                            @Override
//                            public void onDescriptionClickListener(int position) {
//                                //your action
//                            }
//
//                            @Override
//                            public void onTitleIconClickListener(int position) {
//                                //your action
//                            }
//                        }) // Optional Listeners
//                        .build() // Must be called before calling show method
//                        .show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return userStatuses.size();
    }

    public class StatusViewHolder extends RecyclerView.ViewHolder {

        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);

            statusBinding = UserStatusBinding.bind(itemView);
            addStatusBinding = UserAddStatusBinding.bind(itemView);
        }
    }


//    private void userInfo(String userId, int pos) {
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                UserPersonalDetails user = snapshot.getValue(UserPersonalDetails.class);
//                Glide.with(context).load(Objects.requireNonNull(user).getProfileImage()).into(addStatusBinding.AddStatusCircularImage);
//                if (pos != 0) {
//                    Glide.with(context).load(user.getProfileImage()).into(statusBinding.statusSeen);
//                    statusBinding.StatusUserName.setText(user.getFullName());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    private void myStory(TextView textView, ImageView imageView, Boolean click) {
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Stories").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                int count = 0;
//                long currentTime = System.currentTimeMillis();
//                for (DataSnapshot storySnap : snapshot.getChildren()) {
//                    Stories stories = storySnap.getValue(Stories.class);
//
//                    if (stories != null && currentTime > stories.getTimeStart() && currentTime < stories.getTimeEnd()) {
//                        count++;
//                    }
//                }
//
//                if (click) {
//
//                } else {
//                    if (count > 0) {
//                        textView.setText("My Story");
//                        imageView.setVisibility(View.GONE);
//                    } else {
//                        textView.setText("ADD Story");
//                        imageView.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

//    private void seenStory(String userId) {
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Stories").child(userId);
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                int i = 0;
//                for (DataSnapshot snap:snapshot.getChildren()) {
//                    if ()
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        })
//    }
}
