package com.pramod.apartmentrental;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class Signup_activity extends AppCompatActivity {

    EditText mName, mEmail, mPassword, mPhone;
    ImageView mPhoto;
    Spinner mSpinner;
    Button mSignup;
    TextView mBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_activity);

        mName = findViewById(R.id.et_name);
        mEmail = findViewById(R.id.et_name);
        mPassword = findViewById(R.id.et_name);
        mPhone = findViewById(R.id.et_name);
        mPhoto = findViewById(R.id.photo);
        mSignup = findViewById(R.id.btn_signup);
        mBackBtn = findViewById(R.id.back);

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}
