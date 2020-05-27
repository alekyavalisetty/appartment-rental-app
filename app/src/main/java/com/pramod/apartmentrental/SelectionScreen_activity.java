package com.pramod.apartmentrental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pramod.apartmentrental.Admin.AdminDashboard_activity;
import com.pramod.apartmentrental.Renter.RenterDashboard_activity;
import com.pramod.apartmentrental.User.UserDashboard_activity;

import java.util.HashMap;
import java.util.Map;

public class SelectionScreen_activity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private DatabaseReference usersDb;
    private Button mBtnUser;
    private Button mBtnRenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_screen_activity);

        mBtnUser = findViewById(R.id.btn_explore_user);
        mBtnRenter = findViewById(R.id.btn_post_renter);
        mFirebaseAuth = FirebaseAuth.getInstance();
        usersDb = FirebaseDatabase.getInstance().getReference()
                .child("users").child(mFirebaseAuth.getCurrentUser().getUid());


        usersDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.child("role").getValue(String.class).equals("admin"))
                    {
                        Intent intent = new Intent(SelectionScreen_activity.this, AdminDashboard_activity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mBtnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRole("user");
                Intent userIntent = new Intent(SelectionScreen_activity.this, UserDashboard_activity.class);
                userIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(userIntent);
                finish();
            }
        });


        mBtnRenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRole("renter");
                Intent userIntent = new Intent(SelectionScreen_activity.this, RenterDashboard_activity.class);
                userIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(userIntent);
                finish();
            }
        });

    }

    private void changeRole(String role) {
        Map userDetails = new HashMap();
        userDetails.put("role",role);
        usersDb.updateChildren(userDetails);
    }
}
