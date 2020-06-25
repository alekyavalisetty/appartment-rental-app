package com.pramod.apartmentrental.ChatRoom;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.pramod.apartmentrental.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomViewHolder> {

    private List<ChatRoomObject> chatRoomList;
    private Context context;

    public ChatRoomAdapter(List<ChatRoomObject> chatList, Context context){
        this.chatRoomList = chatList;
        this.context = context;
    }


    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View chatRoomLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, null, false);
        LayoutParams chatRoomLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        chatRoomLayout.setLayoutParams(chatRoomLayoutParams);
        return new ChatRoomViewHolder((chatRoomLayout));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position) {

        holder.mChatMessage.setText(chatRoomList.get(position).getChatMessage());

        if(chatRoomList.get(position).getCurrentUser()) {
            holder.mChatMessage.setGravity(Gravity.END);
            holder.mChatRoomContainer.setBackgroundResource(R.color.blur);
        }
        else
        {
            holder.mChatMessage.setGravity(Gravity.START);
            holder.mChatRoomContainer.setBackgroundResource(R.color.transparent);
        }

    }

    @Override
    public int getItemCount() {
        return chatRoomList.size();
    }
}
