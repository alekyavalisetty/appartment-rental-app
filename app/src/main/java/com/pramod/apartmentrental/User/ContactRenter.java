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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pramod.apartmentrental.R;

import java.util.Map;

public class ContactRenter extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    private TextView mUserName, mUserEmail, mUserPhone;
    private Button mCallRenter,mMainRenter;
    private TextView mBack;

    private Uri resultUri;
    private ImageView mProfileImage;
    private String userID, userName,userEmail,usersImageUrl, userPhone, renterId;


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