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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pramod.apartmentrental.Admin.AdminDashboard_activity;
import com.pramod.apartmentrental.Renter.RenterDashboard_activity;
import com.pramod.apartmentrental.User.UserDashboard_activity;

public class Login_activity extends AppCompatActivity {


    EditText mLoginEmail, mLoginPassword;
    Button mLogin;
    TextView mSignup, mForgetPassword;

    String currentUserID;
    String user_role;

    //Firebase Variables to check for sessions
    private FirebaseAuth mFirebaseAuth;

    //db reference
    private DatabaseReference usersDb;

    //Called when change in authentication state
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_login );

        mLoginEmail = findViewById(R.id.et_email);
        mLoginPassword = findViewById(R.id.et_password);
        mLogin = findViewById(R.id.btn_login);

        mSignup = findViewById(R.id.tv_signup);
        mForgetPassword = findViewById(R.id.tv_forget_pwd);


        mFirebaseAuth = FirebaseAuth.getInstance();

        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if(currentUser != null){

                    currentUserID = currentUser.getUid();
                    usersDb = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID);


                    usersDb.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            user_role = dataSnapshot.child("role").getValue(String.class);

                            if(user_role.equals("user")||user_role.equals("renter"))
                            {
                                Intent intent = new Intent(Login_activity.this, SelectionScreen_activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else if(user_role.equals("admin")){
                                Intent intent = new Intent(Login_activity.this, AdminDashboard_activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        };

        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_activity.this, Signup_activity.class);
                startActivity(intent);
            }
        });

        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_activity.this, ForgetPassword_activity.class);
                startActivity(intent);
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mLoginEmail.getText().toString()))
                {
                    mLoginEmail.setError("Please Enter Email Id");
                }
                else if (TextUtils.isEmpty(mLoginPassword.getText().toString()))
                {
                    mLoginPassword.setError("Please Enter Password");
                }
                else
                {
                    String email = mLoginEmail.getText().toString();
                    String password = mLoginPassword.getText().toString();

                    mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login_activity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(!task.isSuccessful()){
                                Toast.makeText(Login_activity.this, "Wrong credentials. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Login_activity.this, "Successfully signed in...", Toast.LENGTH_SHORT).show();
                                showSelectionScreen();
                            }
                        }
                    });

                }
            }

        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(firebaseAuthStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseAuth.removeAuthStateListener(firebaseAuthStateListener);
        finish();
    }

    private void showSelectionScreen() {
        Intent intent = new Intent(Login_activity.this, SelectionScreen_activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}

