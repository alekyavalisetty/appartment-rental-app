package com.pramod.apartmentrental;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileSettings extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    private EditText mUserName, mUserEmail, mUserPhone;
    private Button mSaveChanges,mEditProfile;
    private TextView mBack;

    private Uri resultUri;
    private ImageView mProfileImage;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private String userID, userFirstName, userLastName,userEmail,usersImageUrl, userPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_settings);

        mUserName = findViewById(R.id.username);
        mUserName.setEnabled(false);

        mUserEmail = findViewById(R.id.useremail);
        mUserEmail.setEnabled(false);

        mUserPhone = findViewById(R.id.userphone);
        mUserPhone.setEnabled(false);

        mBack = findViewById(R.id.back);


        mSaveChanges = findViewById(R.id.savechanges);
        mSaveChanges.setVisibility(View.GONE);

        mEditProfile = findViewById(R.id.editprofile);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        mProfileImage = findViewById(R.id.profileimage);
        mProfileImage.setEnabled(false);


        //connecting to database
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(userID);
        getUserInfo();


        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tells the app to go outside the app and not from the app
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

            }
        });

        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditProfile.setVisibility(View.GONE);
                mSaveChanges.setVisibility(View.VISIBLE);
                mProfileImage.setEnabled(true);

                mUserName.setEnabled(true);
                mUserName.requestFocus();

                mUserPhone.setEnabled(true);
                mUserEmail.setEnabled(true);

            }
        });

        mSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;
            }
        });
    }
}