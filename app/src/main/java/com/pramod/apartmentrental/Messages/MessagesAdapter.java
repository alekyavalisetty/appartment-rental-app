package com.pramod.apartmentrental.Messages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pramod.apartmentrental.R;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private List<MessageUserObject> userList;
    private Context context;

    public MessagesAdapter(List<MessageUserObject> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View homeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users_listings, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        homeView.setLayoutParams(lp);
        return new MessageViewHolder((homeView));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        holder.mUserId.setText(userList.get(position).getId());
        holder.mUserName.setText(userList.get(position).getName());
        holder.mUserEmail.setText(userList.get(position).getEmail());
        holder.mUserPhone.setText(userList.get(position).getPhone());

        if(!userList.get(position).getPhoto().equals("")){
            Glide.with(context).load(userList.get(position).getPhoto()).into(holder.mUserImage);
        }


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
