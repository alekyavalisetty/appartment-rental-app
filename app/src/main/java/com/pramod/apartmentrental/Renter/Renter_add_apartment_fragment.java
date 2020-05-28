package com.pramod.apartmentrental.Renter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pramod.apartmentrental.Login_activity;
import com.pramod.apartmentrental.R;

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

    EditText listingName, listingDescription,  listingParking, listingPrice, listingLocation1;

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

    }
}
