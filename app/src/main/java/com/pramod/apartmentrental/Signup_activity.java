package com.pramod.apartmentrental;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Signup_activity extends AppCompatActivity {

    private ArrayAdapter<String> mRoleAdapter;
    EditText mName, mEmail, mPassword, mPhone;
    ImageView mPhoto;
    Spinner mSpinner;
    Button mSignup;
    TextView mBackBtn;

    //Firebase variables for session checking
    FirebaseAuth mFirebaseAuth;
    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_activity);

        //Get Instance which retrives the URL of FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();


        mName = findViewById(R.id.et_name);
        mEmail = findViewById(R.id.et_name);
        mPassword = findViewById(R.id.et_name);
        mPhone = findViewById(R.id.et_name);
        mPhoto = findViewById(R.id.photo);
        mSignup = findViewById(R.id.btn_signup);
        mBackBtn = findViewById(R.id.back);
        mSpinner = findViewById(R.id.spinner_role);

        mRoleAdapter = new ArrayAdapter<>(Signup_activity.this,
                R.layout.spinner_role, getResources().getStringArray(R.array.role));

        mRoleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mRoleAdapter);


        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tells the app to go outside the app and not from the app
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });


        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = mName.getText().toString();
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String phone = mPhone.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    mName.setError("Please enter your First Name");
                }
                else if(TextUtils.isEmpty(email))
                {
                    mEmail.setError("Please enter your Email ID");
                }
                else if(!email.contains("@") || (!email.contains(".")))
                {
                    mEmail.setError("Please enter a valid Email address");
                }
                else if (TextUtils.isEmpty(password)) {
                    mPhone.setError("Please enter your password");
                }
                else if(password.length() < 6){
                    mPassword.setError("Password length is minimum 6 characters.");
                }
                else if(TextUtils.isEmpty(phone))
                {
                    mPhone.setError("Please enter your phone number");
                }
                else if(phone.length() < 10){

                    mPhone.setError("Please enter a valid phone number");
                }
                else {
                    //Database Registration Query

                }

            }
        });


    }
}
