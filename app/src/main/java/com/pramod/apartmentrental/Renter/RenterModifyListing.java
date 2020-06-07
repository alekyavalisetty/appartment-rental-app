package com.pramod.apartmentrental.Renter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pramod.apartmentrental.R;

import java.util.Arrays;
import java.util.List;

public class RenterModifyListing extends AppCompatActivity {

    EditText listingName, listingDescription, listingLocation1, listingPrice,listingLocation;
    ImageButton  getLocation;
    TextView editAptName, editDescription, editImage, editPrice, editLocation,editLocation1, mBack;
    Button saveButton;
    private FirebaseAuth mAuth;
    private String currentUserID,listingID, l_name,l_description, l_city, l_location,l_location1,
            listingURL, l_price,city, address;
    private Double latitude= null, longitude= null;
    private Uri resultUri;
    private ImageView listingImage;
    private DatabaseReference mListingDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renter_modify_listing);

        //Firebase Connections
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        //Get intent extras
        listingID =  getIntent().getExtras().getString("listID");
        mListingDatabase = FirebaseDatabase.getInstance().getReference().child("listings").child(listingID);

        //Linking editText fields
        listingName = findViewById(R.id.listing_name);
        listingDescription = findViewById(R.id.listing_description);
        listingLocation = findViewById(R.id.listing_location);
        listingLocation1 = findViewById(R.id.listing_location1);
        listingPrice = findViewById(R.id.listing_price);
        listingImage = findViewById(R.id.listing_image);


        //Edit Buttons to enable editing on edit texts
        editAptName = findViewById(R.id.edit_name);
        editDescription = findViewById(R.id.edit_description);
        editLocation = findViewById(R.id.edit_location);
        editLocation1 = findViewById(R.id.edit_location1);
        editPrice = findViewById(R.id.edit_price);
        editImage = findViewById(R.id.edit_image);
        mBack = findViewById(R.id.back);
        
        saveButton = findViewById(R.id.button_save_ad);
        saveButton.setVisibility(View.GONE);
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

    }

    private void getApartmentDetails() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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