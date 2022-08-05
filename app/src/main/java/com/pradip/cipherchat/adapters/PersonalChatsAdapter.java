package com.pradip.cipherchat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pradip.cipherchat.R;
import com.pradip.cipherchat.databinding.MessageReceiveBinding;
import com.pradip.cipherchat.databinding.MessageSendBinding;
import com.pradip.cipherchat.model.Message;
import com.pradip.cipherchat.model.UserPersonalDetails;

import java.util.ArrayList;
import java.util.Objects;

public class PersonalChatsAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Message> messages;
    final int ITEM_SENT = 0;
    final int ITEM_RECEIVE = 1;

    public PersonalChatsAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.message_send, parent, false);
            return new messageSentViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.message_receive, parent, false);
            return new messageReceiveViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (Objects.equals(FirebaseAuth.getInstance().getUid(), message.senderId)) {
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        if (holder.getClass() == messageSentViewHolder.class) {
            messageSentViewHolder sendViewHolder = (messageSentViewHolder) holder;
            sendViewHolder.messageSendBinding.SenderMessage.setText(message.getMessage());
        } else {
            messageReceiveViewHolder receiveViewHolder = (messageReceiveViewHolder) holder;
            receiveViewHolder.messageReceiveBinding.ReceiverMessage.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class messageSentViewHolder extends RecyclerView.ViewHolder {

        MessageSendBinding messageSendBinding;

        public messageSentViewHolder(@NonNull View itemView) {
            super(itemView);
            messageSendBinding = MessageSendBinding.bind(itemView);
        }
    }

    public static class messageReceiveViewHolder extends RecyclerView.ViewHolder {

        MessageReceiveBinding messageReceiveBinding;

        public messageReceiveViewHolder(@NonNull View itemView) {
            super(itemView);
            messageReceiveBinding = MessageReceiveBinding.bind(itemView);
        }
    }


}
