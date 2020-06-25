package com.pramod.apartmentrental.ChatRoom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pramod.apartmentrental.R;
import com.pramod.apartmentrental.User.ContactRenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRoom_activity extends AppCompatActivity {


    String mUserPhotoUrl;
    DatabaseReference mUserChatDatabase,mChatRoom, mUserDatabase, mSendMessageDatabase;
    ArrayList<ChatRoomObject> resultsChatMessages = new ArrayList<ChatRoomObject>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatRoomAdapter;
    private RecyclerView.LayoutManager mChatRoomLayoutManager;
    private EditText mSendChatMessage;
    private TextView mUserName;
    private ImageButton mSendBtn, mBackBtn, mViewBtn;
    private String currentUser, receiverId,chatRoomId, chatMessageId, chatMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        uiConnections();
        databaseReferences();
        setUi();

    }

    private void setUi() {

        mUserDatabase.child(receiverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    mUserName.setText(dataSnapshot.child("name").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatRoom_activity.this, ContactRenter.class);
                Bundle b = new Bundle();
                b.putString("renter",receiverId);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        getChatRoomId();

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendChatMessages();
            }
        });

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void getChatRoomId() {
        mUserChatDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    chatRoomId = dataSnapshot.getValue().toString();
                    mChatRoom =  mChatRoom.child(chatRoomId);
                    showChatRoomMessages();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showChatRoomMessages() {
        mChatRoom.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    String chatMessage = null;
                    String sendByUser = null;

                    if(dataSnapshot.child("senderUserId").getValue()!= null){
                        sendByUser = dataSnapshot.child("senderUserId").getValue().toString();
                    }

                    if(dataSnapshot.child("messageContent").getValue()!= null){
                        chatMessage = dataSnapshot.child("messageContent").getValue().toString();
                    }


                    if(chatMessage!=null && sendByUser !=null){
                        Boolean currentUserBoolean = false;

                        if(sendByUser.equals(currentUser)){
                            currentUserBoolean = true;
                        }

                        ChatRoomObject newChat = new ChatRoomObject(currentUserBoolean,chatMessage);
                        resultsChatMessages.add(newChat);
                        mChatRoomAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendChatMessages() {
        chatMessage = mSendChatMessage.getText().toString();

        if(!chatMessage.isEmpty())
        {
            DatabaseReference sendMessage = FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomId).push();
            Map chatMessageMap = new HashMap();
            chatMessageMap.put("senderUserId", currentUser);
            chatMessageMap.put("messageContent", chatMessage);
            sendMessage.setValue(chatMessageMap);
        }

        mSendChatMessage.setText(null);
    }

    private void databaseReferences() {

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mUserChatDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser)
                .child("contacts").child(receiverId).child("chatRoomId");
        mChatRoom = FirebaseDatabase.getInstance().getReference().child("chatrooms");

    }

    List<ChatRoomObject> getChatRoomData() {
        return resultsChatMessages;
    }

    private void uiConnections() {

        receiverId = getIntent().getExtras().getString("renterID");
        chatRoomId = getIntent().getExtras().getString("chatroomId");
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRecyclerView = findViewById(R.id.chatMessageRecycler);
        mBackBtn = findViewById(R.id.back);
        mViewBtn = findViewById(R.id.renter_profile);
        mSendBtn = findViewById(R.id.chat_send);
        mSendChatMessage = findViewById(R.id.chat_message);
        mUserName = findViewById(R.id.renter_name);

        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        mChatRoomLayoutManager = new LinearLayoutManager(ChatRoom_activity.this);
        mRecyclerView.setLayoutManager(mChatRoomLayoutManager);

        mChatRoomAdapter = new ChatRoomAdapter(getChatRoomData(), ChatRoom_activity.this);
        mRecyclerView.setAdapter(mChatRoomAdapter);

    }

}