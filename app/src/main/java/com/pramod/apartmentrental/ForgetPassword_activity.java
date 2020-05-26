package com.pramod.apartmentrental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword_activity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    EditText mUserEmail;
    Button mSend;
    TextView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        mUserEmail = findViewById(R.id.useremail);
        mSend = findViewById(R.id.send);
        mBack = findViewById(R.id.back);

        mAuth = FirebaseAuth.getInstance();

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgetPassword_activity.this, Login_activity.class);
                startActivity(intent);

            }
        });


        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mUserEmail.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter your email id", Toast.LENGTH_SHORT).show();
                    return;
                }

               
            }
        });

    }
}
