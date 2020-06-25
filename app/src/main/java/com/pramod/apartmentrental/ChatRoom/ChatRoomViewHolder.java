package com.pramod.apartmentrental.ChatRoom;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pramod.apartmentrental.R;

public class ChatRoomViewHolder extends RecyclerView.ViewHolder{


    public TextView mChatMessage;
    public LinearLayout mChatRoomContainer;

    public ChatRoomViewHolder(@NonNull final View itemView) {

        super(itemView);
        mChatMessage = itemView.findViewById(R.id.userChat);
        mChatRoomContainer = itemView.findViewById(R.id.chatMessageContainer);
    }
}
