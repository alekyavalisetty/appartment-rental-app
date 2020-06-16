package com.pramod.apartmentrental.Renter.home_listings;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pramod.apartmentrental.R;
import com.pramod.apartmentrental.Renter.RenterModifyListing;

import java.util.ArrayList;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * A simple {@link Fragment} subclass.
 */
public class Renter_home_fragment extends Fragment implements OnMapReadyCallback {

    MapView mMapView;
    //for getting the map view
    View mView;
    String listingID;
    Double latitude, longitude;
    //declaring google Map variables
    private GoogleMap mGoogleMap;
    private MarkerOptions options = new MarkerOptions();
    //for getting latitude and longitude
    private ArrayList<LatLng> latLngs = new ArrayList<>();
    private FirebaseAuth mAuth;
    private String currentUserID;


    public Renter_home_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_renter_home_fragment, container, false);

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        mMapView = mView.findViewById(R.id.renter_Map);

        if(mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

    }

    private void getMapParameters(final GoogleMap googleMap) {

        DatabaseReference listDb = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID).child("listings");

        listDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    for(DataSnapshot showlist : dataSnapshot.getChildren()){
                        FetchListingInformation(showlist.getKey(),googleMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void FetchListingInformation(final String key,final GoogleMap googleMap) {

        DatabaseReference listDb = FirebaseDatabase.getInstance().getReference().child("listings").child(key);
        listDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name= null, snippet = null;
                if (dataSnapshot.child("listing_latitude").getValue() != null) {
                    latitude = Double.parseDouble(dataSnapshot.child("listing_latitude").getValue().toString());
                }

                if (dataSnapshot.child("listing_longitude").getValue() != null) {
                    longitude = Double.parseDouble(dataSnapshot.child("listing_longitude").getValue().toString());
                }

                if (dataSnapshot.child("listing_name").getValue() != null) {
                    name = dataSnapshot.child("listing_name").getValue().toString();
                }
                if (dataSnapshot.child("listing_description").getValue() != null) {
                    snippet = dataSnapshot.child("listing_description").getValue().toString();
                }
                if (dataSnapshot.child("listing_id").getValue() != null) {
                    listingID = dataSnapshot.child("listing_id").getValue().toString();
                }

                latLngs.add(new LatLng(latitude,longitude));

                for(LatLng point : latLngs){
                    options.position(point);
                    options.title(name);
                    options.snippet(snippet);
                    googleMap.addMarker(options);

                }
                mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        Intent intent = new Intent(getActivity(), RenterModifyListing.class);

                        Bundle b = new Bundle();
                        b.putString("listID",listingID);
                        intent.putExtras(b);

                        startActivity(intent);
                        return false;
                    }
                });

                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(latitude, longitude)).zoom(13).bearing(1).tilt(55).build();
                mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap = googleMap;
        getMapParameters(googleMap);

    }
}
