package com.pramod.apartmentrental.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pramod.apartmentrental.R;

import java.util.Map;

public class ApartmentDetails extends AppCompatActivity {

    TextView listingName, listingDescription, listingLocation,
            listingPrice,mBack, listingOwner, mViewMap;
    ImageView mSaveFavourite,listingImage;
    Button contactButton;
    String value, renterID;
    private DatabaseReference mListingDatabase,mOwnerDatabase, mUserDatabase,mUserFavouriteDb;
    private Double latitude, longitude;
    private String currentUserID,listingID, l_name,l_description, l_city, l_location,
            listingURL, l_price, l_owner_name;
    private Uri resultUri;
    private FirebaseAuth mAuth;

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
        listingOwner = findViewById(R.id.ownerName);
        mBack = findViewById(R.id.back);
        mSaveFavourite= findViewById(R.id.favourite_tenants);
        mViewMap= findViewById(R.id.view_in_map);
        listingImage = findViewById(R.id.listing_image);
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

                Intent intent = new Intent(ApartmentDetails.this, UserDashboard_activity.class);
                startActivity(intent);
                finish();
                return;

            }
        });

        mSaveFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mUserFavouriteDb = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID);

                mUserFavouriteDb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                            //get the child you want
                            if (map.get("listing_name") != null) {
                                l_name = map.get("listing_name").toString();
                                listingName.setText(l_name);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                mUserFavouriteDb.child("favourites").child(listingID).child("listing_id").setValue(listingID);
                Toast.makeText(ApartmentDetails.this, "Saved to your Favourites", Toast.LENGTH_SHORT).show();
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


                    if (map.get("listing_image") != "default") {

                        listingURL = map.get("listing_image").toString();

                        Glide.with(getApplication()).load(listingURL).into(listingImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getRenterDetails() {
    }


}