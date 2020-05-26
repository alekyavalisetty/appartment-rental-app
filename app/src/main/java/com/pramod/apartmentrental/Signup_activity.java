package com.pramod.apartmentrental;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Signup_activity extends AppCompatActivity {

    private ArrayAdapter<String> mRoleAdapter;
    EditText mName, mEmail, mPassword, mPhone;
    ImageView mPhoto;
    Spinner mSpinner;
    Button mSignup;
    TextView mBackBtn;
    //Setting up role initially
    private String role, userImageUrl = " ";

    //Firebase variables for session checking
    FirebaseAuth mFirebaseAuth;
    private Uri mResultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_activity);

        //Get Instance which retrives the URL of FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();


        mName = findViewById(R.id.et_name);
        mEmail = findViewById(R.id.et_email);
        mPassword = findViewById(R.id.et_password);
        mPhone = findViewById(R.id.et_phone);

        mPhoto = findViewById(R.id.photo);
        mSignup = findViewById(R.id.btn_signup);
        mBackBtn = findViewById(R.id.back);
        mSpinner = findViewById(R.id.spinner_role);

        mRoleAdapter = new ArrayAdapter<>(Signup_activity.this,
                R.layout.spinner_role, getResources().getStringArray(R.array.role));

        mRoleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mRoleAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position ==0)
                {
                    role = "user";
                }
                else {
                    role = "admin";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

                if (TextUtils.isEmpty(mName.getText().toString())) {
                    mName.setError("Please enter your Name");
                }
                else if(TextUtils.isEmpty(mEmail.getText().toString()))
                {
                    mEmail.setError("Please enter your Email ID");
                }
                else if (TextUtils.isEmpty(mPassword.getText().toString())) {
                    mPassword.setError("Please enter your password");
                }
                else if(mPassword.getText().toString().length() < 6){
                    mPassword.setError("Password length is minimum 6 characters.");
                }
                else if(TextUtils.isEmpty(mPhone.getText().toString())) {
                    mPhone.setError("Please enter your phone number");
                }
                else if(mPhone.getText().toString().length() < 10) {
                    mPhone.setError("Please enter a valid phone number");
                }
                else {

                    final String name = mName.getText().toString();
                    final String email = mEmail.getText().toString();
                    final String password = mPassword.getText().toString();
                    final String phone = mPhone.getText().toString();
                    role = mSpinner.getSelectedItem().toString().trim();
                    //Database Registration Query
                    checkIfEmailAlreadyExists(email);

                    mFirebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            final Map userDetails = new HashMap<>();

                            if(task.isSuccessful()){

                                String currentUserId = mFirebaseAuth.getCurrentUser().getUid();

                                //creates a new user id with all the data in realtime database
                                final DatabaseReference currentUserDbReference = FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(currentUserId);

                                if(mResultUri != null) {

                                    StorageReference filepath = FirebaseStorage.getInstance().getReference().child("user_photos")
                                            .child(currentUserId);

                                    Bitmap bitmap = null;

                                    try {
                                        bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), mResultUri);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    //Reduce image size
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);

                                    byte[] data = baos.toByteArray();

                                    //Upload the image
                                    UploadTask uploadTask = filepath.putBytes(data);

                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            finish();
                                        }
                                    });

                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            if (taskSnapshot.getMetadata() != null) {
                                                if (taskSnapshot.getMetadata().getReference() != null) {
                                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            userImageUrl = uri.toString();
                                                            userDetails.put("photo",userImageUrl);
                                                            currentUserDbReference.updateChildren(userDetails);
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    });

                            }

                                userDetails.put("name",name);
                                userDetails.put("email",email);
                                userDetails.put("role",role);
                                userDetails.put("phone",phone);

                                currentUserDbReference.updateChildren(userDetails);
                                Toast.makeText(Signup_activity.this, "Sign up is successful. Redirecting to selection screen", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(Signup_activity.this, SelectionScreen_activity.class);
                                startActivity(i);
                                finish();

                            }
                        }
                    });
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the intent from activity code is 1
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            final Uri imageUri = data.getData();
            mResultUri = imageUri;
            mPhoto.setImageURI(mResultUri);
        }
    }

    private void checkIfEmailAlreadyExists(String email) {

        mFirebaseAuth.fetchSignInMethodsForEmail(this.mEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                        boolean check = !task.getResult().getSignInMethods().isEmpty();

                        if(!check)
                        {
                            Log.d("Message","Email dont exist.");

                        }
                        else
                        {
                            mEmail.setError("Email is already registered. Try another one.");
                        }
                    }
                });

    }
}
