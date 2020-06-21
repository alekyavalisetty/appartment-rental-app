package com.pramod.apartmentrental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pramod.apartmentrental.Admin.AdminDashboard_activity;
import com.pramod.apartmentrental.Renter.RenterDashboard_activity;
import com.pramod.apartmentrental.User.BlockedUser;
import com.pramod.apartmentrental.User.UserDashboard_activity;

public class Splash_activity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    //db reference
    private DatabaseReference usersDb;

    //Called when change in authentication state
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    String currentUserID;
    String user_role;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        mFirebaseAuth = FirebaseAuth.getInstance();

        final Handler handler = new Handler();



        if(mFirebaseAuth.getCurrentUser()!=null){

            final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if(currentUser != null){

                currentUserID = currentUser.getUid();
                usersDb = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID);
                usersDb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        user_role = dataSnapshot.child("role").getValue(String.class);

                        if(user_role.equals("user"))
                        {
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    Intent intent = new Intent(Splash_activity.this, UserDashboard_activity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 3000);

                        }
                        else if(user_role.equals("renter"))
                        {
                            handler.postDelayed(new Runnable() {
                                public void run() {
                            Intent intent = new Intent(Splash_activity.this, RenterDashboard_activity.class);
                            startActivity(intent);
                            finish();
                                }
                            }, 3000);
                        }
                        else if(user_role.equals("admin")){
                            handler.postDelayed(new Runnable() {
                                public void run() {
                            Intent intent = new Intent(Splash_activity.this, AdminDashboard_activity.class);
                            startActivity(intent);
                            finish();
                                }
                            }, 3000);
                        }

                        else if(user_role.equals("block")){
                            handler.postDelayed(new Runnable() {
                                public void run() {
                            Intent LoginIntent = new Intent(Splash_activity.this, BlockedUser.class);
                            LoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(LoginIntent);
                                }
                            }, 3000);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
      }
      else {
            handler.postDelayed(new Runnable() {
                public void run() {
          Intent LoginIntent = new Intent(Splash_activity.this, Login_activity.class);
          LoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(LoginIntent);
                }
            }, 3000);
      }


    }

    @Override
    protected void onStart() {
        super.onStart();

       // mFirebaseAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
       // mFirebaseAuth.removeAuthStateListener(firebaseAuthStateListener);
    }

}
