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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserProfileSettings extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    private EditText mUserName, mUserPhone;
    private TextView mUserEmail;
    private Button mSaveChanges,mEditProfile, mBlockProfile;
    private TextView mBack;

    private Uri userProfileUri;
    private ImageView mProfileImage;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private String userID, userName,userEmail,usersImageUrl, userPhone, role,roleDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_settings);

        mUserName = findViewById(R.id.username);
        mUserName.setEnabled(false);

        mUserEmail = findViewById(R.id.useremail);
        mUserEmail.setEnabled(false);

        mUserPhone = findViewById(R.id.userphone);
        mUserPhone.setEnabled(false);

        mBack = findViewById(R.id.back);


        mSaveChanges = findViewById(R.id.savechanges);
        mSaveChanges.setVisibility(View.GONE);

        mEditProfile = findViewById(R.id.editprofile);

        mBlockProfile = findViewById(R.id.blockProfile);
        mBlockProfile.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        final Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if(b!=null)
        {
            userID = (String)b.get("renter");
            role = (String)b.get("role");
            mBlockProfile.setVisibility(View.VISIBLE);
            mEditProfile.setVisibility(View.GONE);
        }
        else {
            userID = mAuth.getCurrentUser().getUid();
            role = "user";
        }
        mProfileImage = findViewById(R.id.profileimage);
        mProfileImage.setEnabled(false);


        //connecting to database
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(userID);
        getUserInfo();




        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tells the app to go outside the app and not from the app
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

            }
        });

        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditProfile.setVisibility(View.GONE);
                mSaveChanges.setVisibility(View.VISIBLE);
                mProfileImage.setEnabled(true);

                mUserName.setEnabled(true);
                mUserName.requestFocus();

                mUserPhone.setEnabled(true);

            }
        });

        mBlockProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(roleDb.equals("block"))
                {
                    mUserDatabase.child("role").setValue("user");
                    Toast.makeText(UserProfileSettings.this, "User is unblocked", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    mUserDatabase.child("role").setValue("block");
                    Toast.makeText(UserProfileSettings.this, "User is blocked successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        mSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;
            }
        });
    }

    private void saveUserInformation() {
        userName = mUserName.getText().toString();
        userEmail = mUserEmail.getText().toString();
        userPhone = mUserPhone.getText().toString();

        Map userInfo = new HashMap();

        if(!userName.isEmpty())
        {
            userInfo.put("name", userName);

        }

        if(!userEmail.isEmpty())
        {
            userInfo.put("email", userEmail);

        }

        if(!userPhone.isEmpty())
        {
            userInfo.put("phone", userPhone);

        }

        mUserDatabase.updateChildren(userInfo);

        if (userProfileUri != null) {
            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("photo").child(userID);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), userProfileUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //tomake the image small size
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);

            byte[] data = baos.toByteArray();

            //uploading the image
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
                                    String imageUrl = uri.toString();

                                    Map userInfo = new HashMap();
                                    userInfo.put("photo", imageUrl);

                                    mUserDatabase.updateChildren(userInfo);
                                }
                            });

                        }
                    }



                }
            });
            Toast.makeText(this, "Details are updated successfully.", Toast.LENGTH_SHORT).show();
            finish();
            return;

        } else {
            Toast.makeText(this, "Details are updated successfully.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void getUserInfo() {
        //add a listener to check for current user infromation
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("name") != null) {
                        userName = map.get("name").toString();
                        mUserName.setText(userName);
                    }

                    if (map.get("email") != null) {
                        userEmail = map.get("email").toString();
                        mUserEmail.setText(userEmail);
                    }

                    if (map.get("phone") != null) {
                        userPhone = map.get("phone").toString();
                        mUserPhone.setText(userPhone);
                    }

                    if (map.get("role") != null) {
                        roleDb = map.get("role").toString();
                    }

                    if(roleDb.equals("block"))
                    {
                        mBlockProfile.setText("Unblock");

                    }


                    if (map.get("photo") != null) {

                        usersImageUrl = map.get("photo").toString();

                        switch (usersImageUrl) {

                            case "":
                                Glide.with(getApplication()).load(R.drawable.ic_account).into(mProfileImage);
                                break;
                            default:
                                Glide.with(getApplication()).load(usersImageUrl).into(mProfileImage);
                                break;

                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            userProfileUri = imageUri;
            mProfileImage.setImageURI(userProfileUri);
        }

    }
}