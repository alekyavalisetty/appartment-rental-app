package com.pramod.apartmentrental.Renter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pramod.apartmentrental.Login_activity;
import com.pramod.apartmentrental.R;

import java.util.List;
import java.util.Locale;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * A simple {@link Fragment} subclass.
 */
public class Renter_add_apartment_fragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    private DatabaseReference mListingDatabase,mUserDatabase;
    private String currentUId, aptName, aptLocation, aptParkingDetails, aptDescription, aptPrice, key, aptLocation1 = " ";
    private Double latitude= null, longitude= null;

    private Uri resultUri;
    private ImageView listingImage;
    String imageUrl;

    EditText listingName, listingDescription,  listingPrice, listingLocation1;

    EditText listingBathrooms, listingBedrooms;
    ImageButton bedAdd, bedRemove, bathAdd, bathRemove, getLocation;
    Button postButton;
    AutoCompleteTextView listingLocation;

    private String city, address;

    public Renter_add_apartment_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_renter_add_apartment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();


        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(getActivity(), Login_activity.class);
                    startActivity(intent);
                }
            }
        };

        mListingDatabase = FirebaseDatabase.getInstance().getReference().child("listings");
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users");


        listingName = view.findViewById(R.id.listing_name);
        listingDescription = view.findViewById(R.id.listing_description);
        listingLocation1 = view.findViewById(R.id.listing_location1);

        listingLocation = view.findViewById(R.id.listing_location);
        listingPrice = view.findViewById(R.id.listing_price);
        listingImage = view.findViewById(R.id.listing_image);
        getLocation = view.findViewById(R.id.button_get_location);


        //to select image from galary.
        listingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //tells the app to go outside the app and not from the app
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

            }
        });


       

    }
}
