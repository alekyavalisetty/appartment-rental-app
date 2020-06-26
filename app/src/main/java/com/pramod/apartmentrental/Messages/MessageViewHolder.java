package com.pramod.apartmentrental.Messages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pramod.apartmentrental.ChatRoom.ChatRoom_activity;
import com.pramod.apartmentrental.R;
import com.pramod.apartmentrental.UserProfileSettings;

public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView mUserName, mUserPhone, mUserEmail, mUserId;
    public ImageView mUserImage;
    String chatroomId, renterId, userID;
    DatabaseReference mUserChatDatabase;


    public MessageViewHolder(@NonNull final View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mUserName = itemView.findViewById(R.id.userName);
        mUserPhone = itemView.findViewById(R.id.userPhone);
        mUserEmail = itemView.findViewById(R.id.userEmail);
        mUserId = itemView.findViewById(R.id.userId);
        mUserImage = itemView.findViewById(R.id.userImage);





    }

    private void goToChatRoom(String chatroomId, View itemView) {
        Intent intent = new Intent(itemView.getContext(), ChatRoom_activity.class);
        Bundle b = new Bundle();
        b.putString("renterID",renterId);
        b.putString("chatID",chatroomId);
        intent.putExtras(b);
        itemView.getContext().startActivity(intent);
    }

    @Override
    public void onClick(final View view) {

        //get Chat room id
        mUserChatDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        renterId = mUserId.getText().toString();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mUserChatDatabase.child(userID).child("contacts")
                .child(renterId).child("chatRoomId");

        mUserChatDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    chatroomId = dataSnapshot.getValue().toString();
                    goToChatRoom(chatroomId, view);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
