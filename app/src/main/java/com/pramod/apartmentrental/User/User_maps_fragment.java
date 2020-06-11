package com.pramod.apartmentrental.User;

import android.Manifest;
import android.content.Context;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pramod.apartmentrental.R;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * A simple {@link Fragment} subclass.
 */
public class User_maps_fragment extends Fragment implements OnMapReadyCallback {

    GoogleMap UserGoogleMap;
    MapView mMapView;
    String listingID;
    View mView;
    TextView myCurrentLocation;
    Double latitude, longitude;
    private MarkerOptions markerOptions = new MarkerOptions();


    public User_maps_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_maps_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        myCurrentLocation = view.findViewById(R.id.currentLocation);
        mMapView = view.findViewById(R.id.googleMap);

        if(mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }


        clickListeners();





    }

    private void clickListeners() {
        myCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);

                } else {
                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    //Get latitude/longitude
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    UserGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                    UserGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title("CURRENT USER LOCATION"));
                    CameraPosition currentLocation = CameraPosition.builder().target(new LatLng(latitude, longitude)).zoom(19).bearing(0).tilt(40).build();

                    UserGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentLocation));

                }
            }
        });
    }

    private void getMapParameters(final GoogleMap googleMap) {

        DatabaseReference listDb = FirebaseDatabase.getInstance().getReference().child("listings");

        listDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

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

        UserGoogleMap = googleMap;
        getMapParameters(googleMap);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);

        } else {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            CameraPosition myCameraPosition = CameraPosition.builder().target(new LatLng(latitude, longitude)).zoom(13).bearing(0).tilt(45).build();

            UserGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(myCameraPosition));
        }
    }

}

