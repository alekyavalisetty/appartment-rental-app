package com.pramod.apartmentrental.Renter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pramod.apartmentrental.Admin.AdminDashboard_activity;
import com.pramod.apartmentrental.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RenterModifyListing extends AppCompatActivity {

    EditText listingName, listingDescription, listingLocation1, listingPrice,listingLocation;
    ImageButton  getLocation;
    TextView mBack;
    Button saveButton, deleteButton;
    private FirebaseAuth mAuth;
    private String currentUserID,listingID, l_name,l_description, l_location,l_location1,
            listingURL, l_price,city, address, role, renterId;
    private Double latitude= null, longitude= null;
    private Uri resultUri;
    private ImageView listingImage;
    private DatabaseReference mListingDatabase, mUserDatabase, mAdminDatabase, mUserFavourites;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renter_modify_listing);

        //Firebase Connections
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        //Get intent extras
        listingID =  getIntent().getExtras().getString("listID");

        //check if admin
        mAdminDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID);
        mAdminDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    role = dataSnapshot.child("role").getValue().toString();
                    if(role.equals("admin")){
                        renterId = getIntent().getExtras().getString("renterID");
                        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(renterId).child("listings").child(listingID);
                    }
                    else {
                        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID).child("listings").child(listingID);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mListingDatabase = FirebaseDatabase.getInstance().getReference().child("listings").child(listingID);

        //Linking editText fields
        listingName = findViewById(R.id.listing_name);
        listingDescription = findViewById(R.id.listing_description);
        listingLocation = findViewById(R.id.listing_location);
        listingLocation1 = findViewById(R.id.listing_location1);
        listingPrice = findViewById(R.id.listing_price);
        listingImage = findViewById(R.id.listing_image);

        mBack = findViewById(R.id.back);
        deleteButton = findViewById(R.id.button_delete_ad);
        saveButton = findViewById(R.id.button_save_ad);
        getLocation = findViewById(R.id.button_get_location);
        
        //Get address using maps api
        Places.initialize(this, "AIzaSyDM14aZbu5rVipuAmAZ5ZI24VZ6HWTh0BI");

        listingLocation.setFocusable(false);
        listingLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(RenterModifyListing.this);
                startActivityForResult(intent, 100);
            }
        });
        
        //Back button listener
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        getApartmentDetails();


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListing();
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveApartmentDetails();
            }
        });
    }

    private void deleteListing() {
        mListingDatabase.removeValue();
        mUserDatabase.removeValue();
        //remove from favourites


        Toast.makeText(this, "Apartment removed successfully!", Toast.LENGTH_SHORT).show();

        if(role.equals("admin")){
            Intent homeIntent = new Intent(RenterModifyListing.this, AdminDashboard_activity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
        }
        else {
            Intent homeIntent = new Intent(RenterModifyListing.this, RenterDashboard_activity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
        }

    }

    private void saveApartmentDetails() {

        l_name = listingName.getText().toString();
        l_description = listingDescription.getText().toString();
        l_location = listingLocation.getText().toString();
        l_location1 = listingLocation1.getText().toString();
        l_price = listingPrice.getText().toString();

        //to get lat and long from location
        Geocoder geocoder = new Geocoder(RenterModifyListing.this, Locale.getDefault());

        try{
            List<Address> addresses = geocoder.getFromLocationName(l_location,1);
            if (addresses.size() >0){
                latitude = (addresses.get(0).getLatitude());
                longitude = (addresses.get(0).getLongitude());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map listingInfo = new HashMap();

        if(!l_name.isEmpty())
        {
            listingInfo.put("listing_name", l_name);
        }

        if(!l_description.isEmpty())
        {
            listingInfo.put("listing_description", l_description);
        }

        if(!l_location.isEmpty())
        {
            listingInfo.put("listing_location", l_location);
            listingInfo.put("listing_latitude", latitude);
            listingInfo.put("listing_longitude", longitude);

        }

        if(!l_location1.isEmpty())
        {
            listingInfo.put("listing_location1", l_location1);
        }

        if(!l_price.isEmpty())
        {
            listingInfo.put("listing_price", l_price);
        }

        mListingDatabase.updateChildren(listingInfo);

        //Checking if image url is changed or not
        if (resultUri != null) {
            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("listing_image").child(currentUserID);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
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

                                    Map listingInfo = new HashMap();
                                    listingInfo.put("listing_image", imageUrl);

                                    mListingDatabase.updateChildren(listingInfo);
                                }
                            });

                        }
                    }
                }
            });


        }

        Toast.makeText(this, "Listing is Modified successfully", Toast.LENGTH_SHORT).show();
        finish();

    }

    private void getApartmentDetails() {
        mListingDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    //get the details of renter posted apt details
                    if (map.get("listing_name") != null) {
                        l_name = map.get("listing_name").toString();
                        listingName.setText(l_name);
                    }

                    if (map.get("listing_description") != null) {
                        l_description = map.get("listing_description").toString();
                        listingDescription.setText(l_description);
                    }

                    if (map.get("listing_price") != null) {
                        l_price = map.get("listing_price").toString();
                        listingPrice.setText(l_price);
                    }

                    if (map.get("listing_location") != null) {
                        l_location = map.get("listing_location").toString();
                        listingLocation.setText(l_location);
                    }

                    if (map.get("listing_location1") != null) {
                        l_location1 = map.get("listing_location1").toString();
                        listingLocation1.setText(l_location1);
                    }


                    if (map.get("listing_image") != "default") {

                        listingURL = map.get("listing_image").toString();

                        Glide.with(getApplication()).load(listingURL).placeholder(R.drawable.ic_home).into(listingImage);

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
            resultUri = imageUri;
            listingImage.setImageURI(resultUri);
        }

        if(requestCode == 100 && resultCode == Activity.RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);

            //Set Address in edit text
            listingLocation.setText(place.getAddress());

        }
        else if (resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}