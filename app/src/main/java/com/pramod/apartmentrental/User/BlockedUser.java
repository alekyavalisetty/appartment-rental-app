package com.pramod.apartmentrental.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.pramod.apartmentrental.Login_activity;
import com.pramod.apartmentrental.R;
import com.pramod.apartmentrental.Splash_activity;

public class BlockedUser extends AppCompatActivity {

    Button mLogout;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_user);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mLogout = findViewById(R.id.splashLogout);
        Toast.makeText(BlockedUser.this, "Your account is blocked due to violating our terms and conditions. Please contact support for further assistance.", Toast.LENGTH_LONG).show();

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAuth.signOut();
                Toast.makeText(BlockedUser.this, "User is logged out successfully!", Toast.LENGTH_SHORT).show();
                Intent LoginIntent = new Intent(BlockedUser.this, Login_activity.class);
                LoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(LoginIntent);
                finish();
            }
        });
    }
}