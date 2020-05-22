package com.pramod.apartmentrental;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login_activity extends AppCompatActivity {


    EditText mLoginEmail, mLoginPassword;
    Button mLogin;
    TextView mSignup, mForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_login );

        mLoginEmail = findViewById(R.id.et_email);
        mLoginPassword = findViewById(R.id.et_password);
        mLogin = findViewById(R.id.btn_login);

        mSignup = findViewById(R.id.tv_signup);
        mForgetPassword = findViewById(R.id.tv_forget_pwd);

        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_activity.this, Signup_activity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }
}
