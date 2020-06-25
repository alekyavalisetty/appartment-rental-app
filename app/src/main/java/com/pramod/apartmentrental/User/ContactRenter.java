package com.pramod.apartmentrental.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pramod.apartmentrental.ChatRoom.ChatRoom_activity;
import com.pramod.apartmentrental.R;
import com.pramod.apartmentrental.Renter.RenterModifyListing;

import java.util.Map;

public class ContactRenter extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    private TextView mUserName, mUserEmail, mUserPhone;
    DatabaseReference mUserChatDatabase;
    private TextView mBack;

    private Uri resultUri;
    private ImageView mProfileImage;
    private Button mCallRenter,mMainRenter, mSendMessage;
    private String userID, userName,userEmail,usersImageUrl, userPhone, renterId, chatroomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_renter);

        mUserName = findViewById(R.id.username);
        mUserEmail = findViewById(R.id.useremail);
        mUserPhone = findViewById(R.id.userphone);
        mBack = findViewById(R.id.back);

        mCallRenter = findViewById(R.id.callRenter);
        mMainRenter = findViewById(R.id.mailRenter);
        mSendMessage = findViewById(R.id.sendMessage);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        mProfileImage = findViewById(R.id.profileimage);
        mProfileImage.setEnabled(false);

        final Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if(b!=null)
        {
            renterId = (String)b.get("renter");
        }

        //connecting to database
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(renterId);
        getUserInfo();

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;
            }
        });

        mCallRenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Intent.ACTION_DIAL);
                intent1.setData(Uri.parse("tel:"+userPhone));
                startActivity(intent1);
            }
        });

        mMainRenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mailIntent = new Intent(Intent.ACTION_SEND);
                mailIntent.setType("plain/text");
                mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{userEmail});
                mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Apartment Details");
                mailIntent.putExtra(Intent.EXTRA_TEXT, "Need more details");
                startActivity(mailIntent.createChooser(mailIntent, ""));
            }
        });

        mSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //create chat room
                mUserChatDatabase = FirebaseDatabase.getInstance().getReference().child("users");

                mUserChatDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.child(userID).child("contacts").child(renterId).exists()) {
                            chatroomId = FirebaseDatabase.getInstance().getReference().child("chatrooms").push().getKey();
                            mUserChatDatabase.child(userID).child("contacts")
                                    .child(renterId).child("chatRoomId").setValue(chatroomId);

                            mUserChatDatabase.child(renterId).child("contacts")
                                    .child(userID).child("chatRoomId").setValue(chatroomId);
                            goToChatRoom(chatroomId);
                        }
                        else
                        {
                            mUserChatDatabase.child(userID).child("contacts")
                                    .child(renterId).child("chatRoomId");

                            mUserChatDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists())
                                    {
                                        chatroomId = dataSnapshot.getValue().toString();
                                        goToChatRoom(chatroomId);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }
        });
    }

    private void goToChatRoom(String chatroomId) {
        Intent intent = new Intent(ContactRenter.this, ChatRoom_activity.class);
        Bundle b = new Bundle();
        b.putString("renterID",renterId);
        b.putString("chatID",chatroomId);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void getUserInfo() {

        //add a listener to check for current user infromation
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();


                    if (map.get("name") != null) {
                        userName = map.get("name").toString();
                        mUserName.setText(userName);
                    }

                    if (map.get("email") != null) {
                        userEmail = map.get("email").toString();
                        mUserEmail.setText(userEmail);
                    }

                    if (map.get("phone") != null) {
                        userPhone = map.get("phone").toString();
                        mUserPhone.setText(userPhone);
                    }

                    if (map.get("photo") != null) {

                        usersImageUrl = map.get("photo").toString();

                        switch (usersImageUrl) {

                            case "default":
                                Glide.with(getApplication()).load(R.drawable.ic_home).into(mProfileImage);
                                break;
                            default:
                                Glide.with(getApplication()).load(usersImageUrl).into(mProfileImage);
                                break;

                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}