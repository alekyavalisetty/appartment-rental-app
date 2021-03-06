package com.pramod.apartmentrental.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pramod.apartmentrental.R;
import com.pramod.apartmentrental.User.Favourites.User_favourites_Object;

import java.util.Locale;
import java.util.Map;

public class ApartmentDetails extends AppCompatActivity {

    TextView listingName, listingDescription, listingLocation,
            listingPrice,mBack, listingRenter, mViewMap;
    ImageView mSaveFavourite;
    ImageView listingImage;
    Button contactButton;
    String value, renterID, latitude, longitude;
    private DatabaseReference mListingDatabase, mRenterDatabase, mUserDatabase,mUserFavouriteDb;
    private String currentUserID,listingID, l_name,l_description, l_location,
            listingURL, l_price, l_renter_name;
    private Uri resultUri;
    private FirebaseAuth mAuth;
    Boolean isFavourite = false;
     ImagePopup imagePopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_details);

        mAuth = FirebaseAuth.getInstance();

        currentUserID = mAuth.getCurrentUser().getUid();

        listingID =  getIntent().getExtras().getString("listID");

        //Bind ui
        listingName = findViewById(R.id.listing_name);
        listingDescription = findViewById(R.id.listing_description);
        listingLocation = findViewById(R.id.listing_location);

        listingPrice = findViewById(R.id.listing_price);
        listingRenter = findViewById(R.id.ownerName);
        mBack = findViewById(R.id.back);
        mSaveFavourite= findViewById(R.id.favourite_tenants);
        mViewMap= findViewById(R.id.view_in_map);

        listingImage = findViewById(R.id.listing_image);
        imagePopup = new ImagePopup(this);

        listingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePopup.viewPopup();
            }
        });

        contactButton = findViewById(R.id.contact_owner);


        //connect to db
        mListingDatabase = FirebaseDatabase.getInstance().getReference().child("listings").child(listingID);
        mUserDatabase =  FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID);

        contactButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 mListingDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                         Intent intent = new Intent(ApartmentDetails.this, ContactRenter.class);

                         renterID = dataSnapshot.child("listing_renter_id").getValue(String.class);
                         intent.putExtra("renter", renterID);
                         startActivity(intent);

                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError databaseError) {

                     }
                 });
             }
         });


        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String urlAddress = "http://maps.google.com/maps?q="+ latitude  +"," + longitude +"&iwloc=A&hl=es";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlAddress));
                intent.setPackage("com.google.android.apps.maps");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        mSaveFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mUserFavouriteDb = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID);

                if(!isFavourite)
                {
                    mUserFavouriteDb.child("favourites").child(listingID).child("listing_id").setValue(listingID);
                    Toast.makeText(ApartmentDetails.this, "Saved to your Favourites", Toast.LENGTH_SHORT).show();
                    isFavourite = true;

                }
                else
                {
                    mUserFavouriteDb.child("favourites").child(listingID).removeValue();
                    Toast.makeText(ApartmentDetails.this, "Removed from Favourites", Toast.LENGTH_SHORT).show();
                    isFavourite = false;
                    Intent intent = new Intent(ApartmentDetails.this, UserDashboard_activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }




            }
        });

        getApartmentDetails();
        getRenterDetails();
    }

    private void getApartmentDetails() {
        mListingDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    //get the child you want
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
                        listingPrice.setText("Rent:" + l_price + " $");
                    }

                    if (map.get("listing_location") != null) {
                        l_location = map.get("listing_location").toString();
                        listingLocation.setText(l_location);
                    }
                    if (map.get("listing_latitude") != null) {
                        latitude = map.get("listing_latitude").toString();

                    }
                    if (map.get("listing_longitude") != null) {
                        longitude = map.get("listing_longitude").toString();
                    }


                    if (map.get("listing_image") != "default") {

                        listingURL = map.get("listing_image").toString();

                        Glide.with(getApplication()).load(listingURL).placeholder(R.drawable.ic_home).into(listingImage);
                        imagePopup.initiatePopupWithGlide(listingURL); // Load Image from Drawable
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getRenterDetails() {

        //To get key from database
        mRenterDatabase = FirebaseDatabase.getInstance().getReference().child("listings").child(listingID);


        //To get value from child
        mRenterDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    value = map.get("listing_renter_id").toString();
                    setRenterName(value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void setRenterName(String value) {

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(value);

        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("name") != null) {
                        l_renter_name = map.get("name").toString();
                        listingRenter.setText(l_renter_name);
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

        //if the intent from activity code is 1
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            final Uri imageUri = data.getData();
            resultUri = imageUri;
            listingImage.setImageURI(resultUri);

        }
    }


}