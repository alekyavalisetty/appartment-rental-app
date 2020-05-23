package com.pramod.apartmentrental;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class Signup_activity extends AppCompatActivity {

    private ArrayAdapter<String> mRoleAdapter;
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



    }
}
